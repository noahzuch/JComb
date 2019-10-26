/**
 * Copyright 2019 Noah Zuch noahz97@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.noahzuch.jcomb.core.generator.ipog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.generator.TestGenerator;

/**
 * A TestGenerator implementation that uses the IPOG strategy.
 * @author Noah
 *
 */
public class IpogTestGenerator implements TestGenerator {

  private int[] domainSizes;
  private int parameterCount;
  private int strength;

  private List<int[]> coveringArray;
  private OccurenceTracker occurenceTracker;
  private ConstraintHandler constraintHandler;

  /**
   * Creates a new IpogTestGenerator for the given parameters, strength and constraints.
   * @param strength The strength of the desired covering array
   * @param domainSizes The parameters supplied via an array of their number of values.
   * @param constraintHandler A {@link ConstraintHandler} object for needed constraints.
   */
  public IpogTestGenerator(int strength, int[] domainSizes, ConstraintHandler constraintHandler) {
    this.strength = strength;
    this.domainSizes = domainSizes;
    parameterCount = domainSizes.length;
    this.constraintHandler = constraintHandler;
    occurenceTracker = new OccurenceTracker(domainSizes);
  }

  @Override
  public Stream<int[]> getAllInputCombinations() {
    coveringArray = new LinkedList<>();
    ipogCalculation();
    if (coveringArray.size() == 0) {
      throw new JCombException("No valid test could be calculated. Check defined Constraints");
    }
    return coveringArray.stream();
  }

  private void ipogCalculation() {
    generateFirstParameters();
    for (int parameter = strength; parameter < domainSizes.length; parameter++) {
      CoverageMap covMap = new CoverageMap(strength, domainSizes, parameter + 1);
      horizontalExtension(covMap, parameter);
      verticalExtension(covMap);
    }
  }

  private void generateFirstParameters() {
    int initialTestCount = 1;
    for (int j = 0; j < strength; j++) {
      initialTestCount *= domainSizes[j];
    }
    for (int i = 0; i < initialTestCount; i++) {
      int[] test = Tools.generateNewTest(domainSizes, i, 0, strength);
      if (constraintHandler.isSatisfiable(test)) {
        for (int j = 0; j < strength; j++) {
          occurenceTracker.addUsedValue(j, test[j]);
        }
        coveringArray.add(test);
      }
    }
  }

  private void horizontalExtension(CoverageMap coverageMap,
      int parameterIndex) {
    for (Iterator<int[]> iterator = coveringArray.iterator(); iterator.hasNext();) {
      if (true) { // TODO add seed logik
        int[] test = (int[]) iterator.next();
        int bestValue =
            coverageMap.getBestValueForNewParameter(test, occurenceTracker, constraintHandler);
        test[parameterIndex] = bestValue;
        occurenceTracker.addUsedValue(parameterIndex, bestValue);
        coverageMap.coverValueComb(test);
      }
    }
  }

  private void verticalExtension(CoverageMap coverageMap) {
    Map<Integer, List<int[]>> wildCardTests = new HashMap<>();
    coverageMap.coverRemainingTupels(
        (valueComb, parameterComb) -> verticallyExtendTupel(wildCardTests, valueComb,
            parameterComb));
    fillAllWildCards(wildCardTests);
  }

  private void verticallyExtendTupel(Map<Integer, List<int[]>> wildCardTests, int valueComb,
      int[] parameterComb) {
    int[] test = Tools.generateNewTest(domainSizes, valueComb, parameterComb);
    if (constraintHandler.isSatisfiable(test)) {
      coverComb(wildCardTests, valueComb, parameterComb);
    }
  }

  private void coverComb(Map<Integer, List<int[]>> wildCardTests, int valueCombIndex,
      int[] paramComb) {

    int lastParameterValue = valueCombIndex % domainSizes[parameterCount - 1];

    List<int[]> correctWildCardTests =
        wildCardTests.get(lastParameterValue);
    if (correctWildCardTests == null
        || !coverViaExistingTest(correctWildCardTests, valueCombIndex, paramComb)) {
      // Fallback strategy
      int[] test = Tools.generateNewTest(domainSizes, valueCombIndex, paramComb);
      for (int j = 0; j < paramComb.length; j++) {
        occurenceTracker.addUsedValue(paramComb[j], test[paramComb[j]]);
      }
      coveringArray.add(test);
      if (correctWildCardTests == null) {
        correctWildCardTests = new LinkedList<>();
        wildCardTests.put(lastParameterValue, correctWildCardTests);
      }
      correctWildCardTests.add(test);
    }
  }

  private boolean coverViaExistingTest(List<int[]> wildCardTests, int valueCombIndex,
      int[] paramComb) {
    for (Iterator<int[]> iterator = wildCardTests.iterator(); iterator.hasNext();) {
      int[] test = iterator.next();
      if (checkIfTestCanBeChanged(test, paramComb, valueCombIndex)) {
        updateExistingTest(test, paramComb, valueCombIndex);
        if (!hasStillWildCards(test)) {
          iterator.remove();
        }
        return true;
      }
    }
    return false;
  }

  private boolean checkIfTestCanBeChanged(int[] existingTest, int[] paramComb, int valueCombIndex) {
    int[] toTestTest = Arrays.copyOf(existingTest, existingTest.length);
    for (int i = paramComb.length - 1; i >= 0; i--) {
      int parameterIndex = paramComb[i];
      int paramDomainSize = domainSizes[parameterIndex];
      int x = valueCombIndex % paramDomainSize;
      valueCombIndex = (valueCombIndex - x) / paramDomainSize;
      if (existingTest[paramComb[i]] != x && existingTest[paramComb[i]] != -1) {
        return false;
      } else {
        toTestTest[paramComb[i]] = x;
      }
    }

    return constraintHandler.isSatisfiable(toTestTest);
  }

  private void updateExistingTest(int[] existingTest, int[] paramComb, int valueCombIndex) {
    for (int i = paramComb.length - 1; i >= 0; i--) {
      int parameterIndex = paramComb[i];
      int paramDomainSize = domainSizes[parameterIndex];
      int x = valueCombIndex % paramDomainSize;
      valueCombIndex = (valueCombIndex - x) / paramDomainSize;

      existingTest[paramComb[i]] = x;
      occurenceTracker.addUsedValue(paramComb[i], x);
    }
  }

  private boolean hasStillWildCards(int[] test) {
    for (int i = 0; i < parameterCount; i++) {
      if (test[i] == -1) {
        return true;
      }
    }
    return false;
  }

  private void fillAllWildCards(Map<Integer, List<int[]>> wildCardTests) {
    wildCardTests.values().forEach(tests -> {
      tests.forEach(test -> {
        for (int i = 0; i < test.length; i++) {
          if (test[i] == -1) {
            Iterator<Integer> occIt = occurenceTracker.getOccurenceIterator(i);
            while (occIt.hasNext()) {
              int value = occIt.next();
              test[i] = value;
              if (constraintHandler.isSatisfiable(test)) {
                occurenceTracker.addUsedValue(i, value);
                break;
              }
            }
          }
        }
      });
    });
  }

}

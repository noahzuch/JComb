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

import static com.github.noahzuch.jcomb.core.generator.ipog.Tools.binomCoeff;
import static com.github.noahzuch.jcomb.core.generator.ipog.Tools.getNextParamCombWithFixParam;
import static com.github.noahzuch.jcomb.core.generator.ipog.Tools.packValues;
import static com.github.noahzuch.jcomb.core.generator.ipog.Tools.packValuesExceptLast;
import static com.github.noahzuch.jcomb.core.generator.ipog.Tools.unpackValues;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;

class CoverageMap {

  private int[] domainSizes;
  private int parameterCount;
  private int strength;
  private int parameterCombCount;
  private int[] coverageMap;
  private int[] covMapStartIndizes;
  private int[] covMapCounts;
  private int[] toBeCoveredTupels;

  /**
   * Creates a new CoverageMap with the given strength, parameter domainSizes and the count of
   * parameters that should be considered for checking covered tupels.
   * 
   * @param strength The strength of the tupels.
   * @param domainSizes The domain sizes of all parameters
   * @param toBeConsideredParameterCount Defines how many parameters should be considered in this
   *        map.
   */
  public CoverageMap(int strength, int[] domainSizes, int toBeConsideredParameterCount) {
    this.strength = strength;
    this.domainSizes = domainSizes;
    this.parameterCount = toBeConsideredParameterCount;

    parameterCombCount = binomCoeff(toBeConsideredParameterCount - 1, strength - 1);
    covMapStartIndizes = new int[parameterCombCount];
    covMapCounts = new int[parameterCombCount];
    toBeCoveredTupels = new int[parameterCombCount];

    int[] paramComb = getFirstParameterComb();
    int startIndex = 0;
    for (int i = 0; i < parameterCombCount; i++) {
      covMapStartIndizes[i] = startIndex;
      int valueCombCount = 1;
      for (int j = 0; j < paramComb.length; j++) {
        valueCombCount *= domainSizes[paramComb[j]];
      }
      toBeCoveredTupels[i] = valueCombCount;
      startIndex += valueCombCount;
      covMapCounts[i] = valueCombCount;
      getNextParamCombWithFixParam(paramComb);
    }
    int valueCoverageSize = (int) Math.ceil(startIndex / 32f);
    if (valueCoverageSize < 1) {
      valueCoverageSize = 1;
    }
    coverageMap = new int[valueCoverageSize];
  }

  /**
   * Sets the tupels covered by the given test to covered in this coverage map.
   * 
   * @param test The test which defines the covered tupels.
   */
  public void coverValueComb(int[] test) {
    int[] paramComb = getFirstParameterComb();
    for (int paramCombIndex = 0; paramCombIndex < parameterCombCount; paramCombIndex++) {
      if (toBeCoveredTupels[paramCombIndex] != 0) {
        int startIndex = covMapStartIndizes[paramCombIndex];
        int coverageIndex = packValues(domainSizes, test, paramComb) + startIndex;
        if (isNotCovered(coverageIndex)) {
          cover(coverageIndex);
          toBeCoveredTupels[paramCombIndex]--;
        }
      }
      getNextParamCombWithFixParam(paramComb);
    }
  }

  /**
   * Retrieves the value for the newest parameter (with index {@link #parameterCount}-1) in the
   * given test, that covers the most uncovered tupels.
   * 
   * @param test The test to get the best new value for.
   * @return the best value for the the new parameter.
   */
  public int getBestValueForNewParameter(int[] test, OccurenceTracker occTracker,
      ConstraintHandler constraintHandler) {
    int[] gains = new int[domainSizes[parameterCount - 1]];
    int[] paramComb = getFirstParameterComb();
    for (int i = 0; i < parameterCombCount; i++) {
      if (toBeCoveredTupels[i] != 0) {
        int startIndex = covMapStartIndizes[i];
        int baseCoverageIndex = startIndex
            + packValuesExceptLast(domainSizes, test, paramComb) * domainSizes[parameterCount - 1];
        // Iterator<Integer> valuesToCheck = occTracker.getOccurenceIterator(parameterCount - 1);
        // while (valuesToCheck.hasNext()) {
        for (int j = 0; j < domainSizes[parameterCount - 1]; j++) {
          // int j = valuesToCheck.next();
          int coverageIndex = baseCoverageIndex + j;
          if (isNotCovered(coverageIndex)) {
            gains[j]++;
          }
        }
      }
      getNextParamCombWithFixParam(paramComb);
    }
    /*
     * int maxIndex = 0; Iterator<Integer> valuesToCheck =
     * occTracker.getOccurenceIterator(parameterCount - 1); while (valuesToCheck.hasNext()) { int j
     * = valuesToCheck.next(); test[parameterCount - 1] = j; if
     * (constraintHandler.isSatisfiable(test)) { maxIndex = j; break; } }
     * 
     * while (valuesToCheck.hasNext()) { int j = valuesToCheck.next(); if (gains[j] >
     * gains[maxIndex]) { test[parameterCount - 1] = j; if (constraintHandler.isSatisfiable(test)) {
     * maxIndex = j; } } }
     */
    Iterator<Integer> occurenceIterator = occTracker.getOccurenceIterator(parameterCount - 1);

    // find first value that satisfies constraints before comparing gains
    int bestValue = -1;
    boolean foundFirstValid = false;
    while (!foundFirstValid && occurenceIterator.hasNext()) {
      int value = occurenceIterator.next();
      test[parameterCount - 1] = value;
      if (constraintHandler.isSatisfiable(test)) {
        bestValue = value;
        foundFirstValid = true;
      }
    }

    while (occurenceIterator.hasNext()) {
      int value = occurenceIterator.next();
      if (gains[value] > gains[bestValue]) {
        test[parameterCount - 1] = value;
        if (constraintHandler.isSatisfiable(test)) {
          bestValue = value;
        }
      }
    }

    /*
     * int maxIndex = 0; for (int i = 0; i < gains.length; i++) { test[parameterCount - 1] = i; if
     * (constraintHandler.isSatisfiable(test)) { maxIndex = i; break; } } for (int i = maxIndex + 1;
     * i < gains.length; i++) { if (gains[i] > gains[maxIndex]) { test[parameterCount - 1] = i; if
     * (constraintHandler.isSatisfiable(test)) { maxIndex = i; } } }
     */

    return bestValue;
  }

  /**
   * Sets all remaining tupels to covered and calls the given consumer for each of them.
   * 
   * @param consumer A consumer that has to ensure that the supplied tupel is covered in the
   *        covering array.
   */
  public void coverRemainingTupels(BiConsumer<Integer, int[]> consumer) {
    int[] paramComb = getFirstParameterComb();
    for (int i = 0; i < parameterCombCount; i++) {
      if (toBeCoveredTupels[i] != 0) {
        int startIndex = covMapStartIndizes[i];
        for (int valueCombIndex = 0; valueCombIndex < covMapCounts[i]; valueCombIndex++) {
          int coverageIndex = startIndex + valueCombIndex;
          if (isNotCovered(coverageIndex)) {
            consumer.accept(valueCombIndex, paramComb);
            cover(coverageIndex);
          }
        }
      }
      getNextParamCombWithFixParam(paramComb);
    }
  }

  private boolean isNotCovered(int coverageIndex) {
    return ((coverageMap[coverageIndex / 32] >>> (coverageIndex % 32)) & 1) == 0;
  }

  private void cover(int coverageIndex) {
    coverageMap[coverageIndex / 32] |= 1 << (coverageIndex % 32);
  }

  private int[] getFirstParameterComb() {
    return Tools.getFirstParamCombWithFixParam(strength, parameterCount);
  }

  @Override
  public String toString() {
    List<StringBuilder> builders = new LinkedList<StringBuilder>();
    int[] comb = getFirstParameterComb();
    for (int paramCombIndex = 0; paramCombIndex < parameterCombCount; paramCombIndex++) {
      int valueCombCount = 1;
      for (int j = 0; j < comb.length; j++) {
        valueCombCount *= domainSizes[comb[j]];
      }
      for (int valueCombIndex = 0; valueCombIndex < valueCombCount; valueCombIndex++) {
        StringBuilder builder = null;
        if (valueCombIndex < builders.size()) {
          builder = builders.get(valueCombIndex);
        } else {
          builder = new StringBuilder();
          builders.add(builder);
        }
        builder.append('[');
        int[] valueComb = unpackValues(domainSizes, valueCombIndex, parameterCount, comb);
        int paramIndex = 0;
        for (int i = 0; i < valueComb.length; i++) {
          if (i == comb[paramIndex]) {
            builder.append(valueComb[i]);
            if (paramIndex < comb.length - 1) {
              paramIndex++;
            }
          } else {
            builder.append('-');
          }
          if (i < valueComb.length - 1) {
            builder.append(',');
          }
        }
        builder.append(']');
        builder.append(" : ");
        int coverageIndex = valueCombIndex + covMapStartIndizes[paramCombIndex];
        if (isNotCovered(coverageIndex)) {
          builder.append('0');
        } else {
          builder.append('1');
        }
        builder.append("   ");
      }
      getNextParamCombWithFixParam(comb);
    }
    StringBuilder finalBuilder = new StringBuilder();
    builders.forEach(builder -> finalBuilder.append(builder.toString() + "\n"));
    return finalBuilder.toString();
  }
}

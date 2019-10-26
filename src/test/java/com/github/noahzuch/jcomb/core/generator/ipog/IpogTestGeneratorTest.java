/**
 * Copyright 2019 Noah Zuch noahz97@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.noahzuch.jcomb.core.generator.ipog;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.constraint.tree.ConstraintTree;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;
import com.github.noahzuch.jcomb.junitjupiter.SmartToString;

@ExtendWith(JCombExtension.class)
public class IpogTestGeneratorTest {

  public static final int DOMAIN_SIZES = 0;

  @Parameter(DOMAIN_SIZES)
  private static Values domainSizes = new Values(
      new int[] {2, 2, 2},
      new int[] {3, 3, 3, 3, 3},
      new int[] {2, 3, 4, 5, 4},
      new int[] {7, 6, 5, 4, 3, 2},
      new int[] {2, 3, 4, 5, 6, 7});

  @Parameter(1)
  private static Ints strengths = new Ints(2, 3, 4);

  @Parameter(2)
  private static Values constraints = new Values(
      null,
      new com.github.noahzuch.jcomb.core.constraint.Constraint() {

        int[] params = new int[] {0, 2};

        public String toString() {
          return "1";
        }

        @Override
        public int[] getInvolvedParameters() {
          return params;
        }

        @Override
        public boolean confirmsWith(Object[] inputCombination) {
          return ((int) inputCombination[0]) > ((int) inputCombination[1]);
        }
      },
      new com.github.noahzuch.jcomb.core.constraint.Constraint() {

        int[] params = new int[] {0, 1, 2};

        public String toString() {
          return "2";
        }

        @Override
        public int[] getInvolvedParameters() {
          return params;
        }

        @Override
        public boolean confirmsWith(Object[] inputCombination) {
          return ((int) inputCombination[0])
              + ((int) inputCombination[1]) < ((int) inputCombination[2]);
        }
      });

  @Constraint(id = 0, parameters = {0, 1})
  static boolean checkStrengthNotBiggerThanParamCount(int[] domainSizes, int strength) {
    return domainSizes.length >= strength;
  }

  @JCombTest()
  void testGeneration(int[] domainSizes, int strength,
      com.github.noahzuch.jcomb.core.constraint.Constraint constraint) {
    ConstraintHandler constraintHandler = null;
    if (constraint != null) {
      JCombContext context = mock(JCombContext.class);
      when(context.getConstraints()).thenReturn(Collections.singletonMap(0, constraint));
      when(context.getParameterCount()).thenReturn(domainSizes.length);
      for (int i = 0; i < domainSizes.length; i++) {
        when(context.getParameter(i))
            .thenReturn(
                new Ints(IntStream.range(0, domainSizes[i]).boxed().collect(Collectors.toList())
                    .toArray(new Integer[domainSizes[i]])));
        System.out.println(SmartToString.toString(context.getParameter(i)));
      }
      constraintHandler = new ConstraintTree(context);
    } else {
      constraintHandler = ConstraintHandler.getDefaultConstraintHandler();
    }
    IpogTestGenerator generator = new IpogTestGenerator(strength, domainSizes, constraintHandler);
    List<int[]> result = generator.getAllInputCombinations().collect(Collectors.toList());
    System.out.println("Size: " + result.size());
    result.forEach(IpogTestGeneratorTest::printTest);
    assertCoveringArray(domainSizes, result, strength, constraintHandler);
  }

  /**
   * Checks if a given matrix is a covering array.
   * 
   * @param domainSizes The parameters as an array of their domain sizes.
   * @param coveringArray The matrix to check.
   * @param strength The strength of the covering array.
   * @param constraintHandler A {@link ConstraintHandler} object containing the required
   *        constraints.
   */
  public static void assertCoveringArray(int[] domainSizes,
      List<int[]> coveringArray, int strength, ConstraintHandler constraintHandler) {
    int[] paramComb = Tools.getFirstParamComb(strength);
    for (int i = 0; i < Tools.binomCoeff(domainSizes.length, strength); i++) {
      int valueCombCount = 1;
      for (int j = 0; j < paramComb.length; j++) {
        valueCombCount *= domainSizes[paramComb[j]];
      }
      for (int valueIndex = 0; valueIndex < valueCombCount; valueIndex++) {
        int[] testWithWhitecard = Tools.generateNewTest(domainSizes, valueIndex, paramComb);
        if (constraintHandler == null || constraintHandler.isSatisfiable(testWithWhitecard)) {
          checkForExistingTestcase(testWithWhitecard, coveringArray);
        }
      }
      Tools.getNextParamComb(paramComb);
    }

  }

  private static void checkForExistingTestcase(int[] testWithWhitespaces,
      List<int[]> combinations) {
    for (int[] test : combinations) {
      for (int i = 0; i < testWithWhitespaces.length; i++) {
        if (containsWhiteSpaceTest(test, testWithWhitespaces)) {
          return;
        }
      }
    }
    fail(
        "OrthogonalArrayTestGenerator did not produce a test that covers the following combination:"
            + Arrays.toString(testWithWhitespaces));

  }

  private static boolean containsWhiteSpaceTest(int[] test, int[] testWithWhitespaces) {
    for (int i = 0; i < testWithWhitespaces.length; i++) {
      if (testWithWhitespaces[i] != -1 && testWithWhitespaces[i] != test[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Prints the test to System.out.
   * 
   * @param test The test to print.
   */
  public static void printTest(int[] test) {
    int totalSpaces = 2;
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (int i = 0; i < test.length; i++) {
      int value = test[i];
      int spaces = String.valueOf(value).length();
      if (spaces < totalSpaces) {
        for (int j = spaces; j < totalSpaces; j++) {
          builder.append(' ');
        }
      }
      builder.append(value);
      if (i != test.length - 1) {
        builder.append(',');
      }
    }
    builder.append(']');
    System.out.println(builder.toString());
  }

}

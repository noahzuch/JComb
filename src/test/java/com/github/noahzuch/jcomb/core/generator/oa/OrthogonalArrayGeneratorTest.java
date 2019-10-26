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
package com.github.noahzuch.jcomb.core.generator.oa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.core.generator.Algorithm;
import com.github.noahzuch.jcomb.core.generator.TestGenerator;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;

@ExtendWith(JCombExtension.class)
class OrthogonalArrayGeneratorTest {

  @Parameter(0)
  private static Values valueCount = new Values(3, 4, 5, 7, 8, 11, 13, 17, 13);

  @Parameter(1)
  private static Ints parameterCount = new Ints(2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15);

  @Constraint(id = 0, parameters = {1, 0})
  public static boolean checkCorrectParameterValueCount(int parameter, int value) {
    return parameter < value + 2;
  }

  @Constraint(id = 1, parameters = {1, 0})
  public static boolean checkInCorrectParameterValueCount(int parameter, int value) {
    return !checkCorrectParameterValueCount(parameter, value);
  }

  @JCombTest(algorithm = Algorithm.IPOG, constraints = {0})
  void testOrthogonalArrayGenerationWithPrimePowers(int valueCount, int parameterCount) {
    testOrthogonalArrayGeneration(valueCount, parameterCount);
  }

  @Test
  void testOrthogonalArrayGenerationWithNonPrimePower12() {
    testOrthogonalArrayGeneration(12, 4);
  }

  @Test
  void testOrthogonalArrayGenerationWithNonPrimePower20() {
    testOrthogonalArrayGeneration(20, 5);
  }

  @Test
  void testOrthogonalArrayGenerationWithNonPrimePower60() {
    testOrthogonalArrayGeneration(60, 4);
  }

  void testOrthogonalArrayGeneration(int valueCount, int parameterCount) {

    OrthogonalArrayGenerator generator = new OrthogonalArrayGenerator(parameterCount, valueCount);

    List<int[]> combinations = generator.getAllInputCombinations().collect(Collectors.toList());
    // combinations.forEach(OrthogonalArrayGeneratorTest::printTest);
    assertEquals((int) Math.pow(valueCount, 2), combinations.size(),
        "The amount of tests is not correct");
    assertOrthogonalArray(parameterCount, valueCount, combinations);
  }

  //

  @JCombTest(algorithm = Algorithm.IPOG, constraints = {1})
  void testFailureParameterCountToBig(int valueCount, int parameterCount) {

    try {
      new OrthogonalArrayGenerator(parameterCount, valueCount);
      fail("No Exception when parameterCount is to big for given valueCount");
    } catch (IllegalArgumentException e) {
      assertEquals(
          "The algorithm Orthogonal Array can not work with the number of parameters being higher"
          + " than the number of parametervalues plus one",
          e.getMessage());
    }
  }

  @Test
  void testFailureLessThanTwoParameters() {
    try {
      new OrthogonalArrayGenerator(1, 3);
      fail("No Exception when creating OA generator with only 1 parameter");
    } catch (IllegalArgumentException e) {
      assertEquals("Algorithm Orhtogonal Array needs at least two parameter.", e.getMessage());
    }
  }

  @Test
  void testOrthogonalArrayWith3ParametersNoPrimePower() {
    TestGenerator generator = new OrthogonalArrayGenerator(3, 12);

    List<int[]> coveringArray = generator.getAllInputCombinations().collect(Collectors.toList());
    assertEquals((int) Math.pow(12, 2), coveringArray.size(),
        "The amount of tests is not correct");
    assertOrthogonalArray(3, 12, coveringArray);
  }

  private void assertOrthogonalArray(int parameterCount, int valueCount,
      List<int[]> coveringArray) {
    for (int p1 = 0; p1 < parameterCount; p1++) {
      for (int p2 = p1 + 1; p2 < parameterCount; p2++) {
        for (int v1 = 0; v1 < valueCount; v1++) {
          for (int v2 = 0; v2 < valueCount; v2++) {
            checkForSingleExistingTestcase(p1, v1, p2, v2, coveringArray);
          }
        }
      }
    }
  }

  private void checkForSingleExistingTestcase(int p1, int v1, int p2, int v2,
      List<int[]> combinations) {
    boolean found = false;
    for (int[] test : combinations) {
      if (test[p1] == v1 && test[p2] == v2) {
        if (!found) {
          found = true;
        } else {
          fail(
              "OrthogonalArrayTestGenerator produced two tests that cover following combination: ("
                  + p1 + ", " + p2 + ") with values: (" + v1 + ", " + v2 + ")");
        }
        return;
      }
    }
    if (!found) {
      fail(
          "OrthogonalArrayTestGenerator did not produce a test that covers the combination: ("
              + p1 + ", " + p2 + ") with values: (" + v1 + ", " + v2 + ")");
    }
  }

  // public static void printTest(int[] test) {
  // int totalSpaces = 2;
  // StringBuilder builder = new StringBuilder();
  // builder.append("[");
  // for (int i = 0; i < test.length; i++) {
  // int value = test[i];
  // int spaces = String.valueOf(value).length();
  // if (spaces < totalSpaces) {
  // for (int j = spaces; j < totalSpaces; j++) {
  // builder.append(' ');
  // }
  // }
  // builder.append(value);
  // if (i != test.length - 1) {
  // builder.append(',');
  // }
  // }
  // builder.append(']');
  // System.out.println(builder.toString());
  // }

}

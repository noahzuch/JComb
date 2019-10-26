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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.core.generator.ipog.Tools;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;

@ExtendWith(JCombExtension.class)
class ToolsTest {

  @Parameter(0)
  private static Values indizes = new Values(0, 5, 10, 15, 16, 20);

  @Parameter(1)
  private static Values parameters =
      new Values(new int[] {0, 1, 2}, new int[] {1, 2, 3}, new int[] {3, 4, 5},
          new int[] {0, 1, 5}, new int[] {0, 2, 4}, new int[] {2, 4, 5});

  @Parameter(2)
  private static Values domainSizes = new Values(new int[] {2, 2, 2, 2, 2, 2},
      new int[] {2, 2, 3, 3, 3, 3}, new int[] {10, 5, 3, 10, 11, 7},
      new int[] {1, 1, 1, 1, 1, 1}, new int[] {1, 2, 3, 4, 5, 6}, new int[] {5, 5, 5, 5, 5, 2});

  @Constraint(id = 0, parameters = {0, 1, 2})
  static boolean isIndexValid(int index, int[] params, int[] domainSizes) {
    int maxIndexPlus1 = 1;
    for (int i = 0; i < params.length; i++) {
      maxIndexPlus1 *= domainSizes[params[i]];
    }
    return index < maxIndexPlus1;
  }

  @JCombTest(parameters = {0, 1, 2})
  void testPackUnpack(int index, int[] params, int[] domainSizes) {
    assertEquals(index,
        Tools.packValues(domainSizes, Tools.unpackValues(domainSizes, index, 6, params), params));
  }

  @Test
  void testSinglePackUnpack() {
    testPackUnpack((int) indizes.getValueAt(3), (int[]) parameters.getValueAt(3),
        (int[]) domainSizes.getValueAt(2));
  }

  @JCombTest(parameters = {0, 1, 2})
  void testPackUnpackExceptLast(int index, int[] params, int[] domainSizes) {
    int[] tupel = Tools.unpackValues(domainSizes, index, 6, params);
    int calculatedIndex = Tools.packValuesExceptLast(domainSizes, tupel, params);
    // add last parameter value manually
    calculatedIndex =
        calculatedIndex * domainSizes[params[params.length - 1]] + tupel[params[params.length - 1]];
    assertEquals(index, calculatedIndex);
  }

  @Test
  void testGenerateNewTestWithParamComb() {
    int[] test = Tools.generateNewTest(new int[] {3, 4, 5}, 14, new int[] {0, 2});
    assertEquals(2, test[0]);
    assertEquals(-1, test[1]);
    assertEquals(4, test[2]);
  }

  @Test
  void testGenerateNewTestWithParamOffsetAndLength() {
    int[] test = Tools.generateNewTest(new int[] {3, 6, 3}, 13, 1, 2);
    assertEquals(-1, test[0]);
    assertEquals(4, test[1]);
    assertEquals(1, test[2]);
  }

  @Parameter(3)
  private static Values paramCombs = new Values(
      vector(new int[] {0, 1, 2}, new int[] {0, 1, 3}),
      vector(new int[] {0, 1, 3}, new int[] {0, 2, 3}),
      vector(new int[] {5, 7, 9}, new int[] {6, 7, 9}),
      vector(new int[] {6, 7, 9}, new int[] {0, 8, 9}),
      vector(new int[] {4, 5, 6}, new int[] {0, 1, 7}));

  @Parameter(4)
  private static Values paramCombsWithFixParam = new Values(
      vector(new int[] {0, 1, 2, 4}, new int[] {0, 1, 3, 4}),
      vector(new int[] {0, 1, 3, 4}, new int[] {0, 2, 3, 4}),
      vector(new int[] {5, 7, 9, 10}, new int[] {6, 7, 9, 10}),
      vector(new int[] {6, 7, 9, 10}, new int[] {0, 8, 9, 10}),
      vector(new int[] {4, 5, 6, 8}, new int[] {0, 1, 7, 8}));

  private static <E> Vector<E> vector(@SuppressWarnings("unchecked") E... objects) {
    List<E> list = Arrays.asList(objects);
    return new Vector<E>(list);
  }

  @JCombTest(strength = 1, parameters = {3}, ignoreConstraints = true)
  void testGetNextParamComb(Vector<int[]> paramCombs) {
    Tools.getNextParamComb(paramCombs.get(0));
    assertArrayEquals(paramCombs.get(1), paramCombs.get(0));
  }

  @JCombTest(strength = 1, parameters = {4}, ignoreConstraints = true)
  void testGetNextParamCombWithFixParam(Vector<int[]> paramCombs) {
    Tools.getNextParamCombWithFixParam(paramCombs.get(0));
    assertArrayEquals(paramCombs.get(1), paramCombs.get(0));
  }

  @Test
  void testBinomCoeff() {
    System.out.println(Tools.binomCoeff(20, 1));
  }

}

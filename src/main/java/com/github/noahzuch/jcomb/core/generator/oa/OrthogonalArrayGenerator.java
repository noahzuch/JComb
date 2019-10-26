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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.generator.Algorithm;
import com.github.noahzuch.jcomb.core.generator.TestGenerator;
import com.github.noahzuch.jcomb.core.oa.finitefield.FiniteField;

/**
 * A {@link TestGenerator} that implements the algorithm Orthogonal Array.
 * 
 * @author Noah Zuch
 *
 */
public class OrthogonalArrayGenerator implements TestGenerator {

  private int parameterCount;
  private int valueCount;

  /**
   * Creates a new generator. The supplied context has to follow the restrictions described in
   * {@link Algorithm#OA}.
   * 
   * @throws JCombException if the orthogonal array construction is not possible for the supplied
   *         parameters.
   */
  public OrthogonalArrayGenerator(int parameterCount, int valueCount) {
    assert parameterCount > 0;

    this.parameterCount = parameterCount;
    this.valueCount = valueCount;

    checkInput(parameterCount, valueCount);
  }

  @Override
  public Stream<int[]> getAllInputCombinations() {
    assert parameterCount >= 2;

    if (parameterCount == 2) {
      return getAllInputFor2Param();
    } else if (parameterCount == 3) {
      return getAllInputFor3Param();
    } else {
      return getAllInputForAnyParamTest();
    }
  }

  private Stream<int[]> getAllInputFor2Param() {
    assert parameterCount == 2;

    List<int[]> result = new ArrayList<>(valueCount ^ 2);
    for (int i = 0; i < valueCount; i++) {
      for (int j = 0; j < valueCount; j++) {
        int[] test = new int[parameterCount];
        test[0] = i;
        test[1] = j;
        result.add(test);
      }
    }

    return result.stream();
  }

  private Stream<int[]> getAllInputFor3Param() {
    assert parameterCount == 3;

    List<int[]> result = new ArrayList<>(valueCount ^ 2);
    for (int i = 0; i < valueCount; i++) {
      for (int j = 0; j < valueCount; j++) {
        int[] test = new int[parameterCount];
        test[0] = i;
        test[1] = j;
        test[2] = (i + j) % valueCount;
        result.add(test);
      }
    }

    return result.stream();
  }

  private Stream<int[]> getAllInputForAnyParamTest() {
    List<FiniteField> finiteFields = Tools.getFiniteFieldsViaPrimeFactors(valueCount);
    // TODO move this check so that it gets catched when creating the OA Generator
    finiteFields.stream().forEach(ff -> {
      if (parameterCount > ff.size() + 1) {
        throw new IllegalArgumentException(
            "The algorithm Orthogonal Array can not work if the parameter count is bigger than any "
            + "prime power of the value count plus 1");
      }
    });
    int arraySize = (int) Math.pow(valueCount, 2);
    List<int[]> result = new ArrayList<>(arraySize);
    for (int i = 0; i < arraySize; i++) {
      result.add(new int[parameterCount]);
    }
    fillOrthogonalArray(result, finiteFields, 0, new int[parameterCount], 0);

    return result.stream();
  }

  private int fillOrthogonalArray(List<int[]> orthogonalArray, List<FiniteField> finiteFields,
      int fieldIndex, int[] valueOffsets, int testIndex) {
    FiniteField field = finiteFields.get(fieldIndex);
    for (int i = 0; i < field.size(); i++) {
      for (int j = 0; j < field.size(); j++) {
        if (fieldIndex == finiteFields.size() - 1) {
          for (int k = 0; k < parameterCount; k++) {
            orthogonalArray.get(testIndex)[k] =
                computeValue(field, k, i, j) + valueOffsets[k];
          }
          testIndex++;
        } else {
          int[] newValueOffsets = new int[parameterCount];
          for (int k = 0; k < parameterCount; k++) {
            newValueOffsets[k] = finiteFields.get(fieldIndex + 1).size()
                * (valueOffsets[k] + computeValue(field, k, i, j));
          }
          testIndex =
              fillOrthogonalArray(orthogonalArray, finiteFields, fieldIndex + 1, newValueOffsets,
                  testIndex);
        }
      }
    }
    return testIndex;
  }

  private int computeValue(FiniteField field, int parameter, int i, int j) {
    if (parameter == 0) {
      return i;
    } else if (parameter == 1) {
      return j;
    } else {
      return field.add(j, field.multiply(parameter - 1, i));
    }
  }

  private void checkInput(int k, int v) {
    if (k < 2) {
      throw new IllegalArgumentException(
          "Algorithm Orhtogonal Array needs at least two parameter.");
    }

    if (k > 3) {
      if (k > v + 1) {
        throw new IllegalArgumentException(
            "The algorithm Orthogonal Array can not work with the number of parameters being higher"
            + " than the number of parametervalues plus one");
      }
    }
  }
}

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
package com.github.noahzuch.jcomb.core.oa.finitefield;

/**
 * An implementation of the {@link FiniteField} interface that uses saved field tables.
 * @author Noah
 *
 */
public class SavedFiniteField implements FiniteField {

  private static final FiniteFieldTable[] finiteFields = new FiniteFieldTable[] {

      new FiniteFieldTable(4, new int[][] {{0, 1, 2, 3},
          {1, 0, 3, 2},
          {2, 3, 0, 1},
          {3, 2, 1, 0}},
          new int[][] {{0, 0, 0, 0},
              {0, 1, 2, 3},
              {0, 2, 3, 1},
              {0, 3, 1, 2}}),
      new FiniteFieldTable(8,
          new int[][] {{0, 1, 2, 3, 4, 5, 6, 7},
              {1, 0, 3, 2, 5, 4, 7, 6},
              {2, 3, 0, 1, 6, 7, 4, 5},
              {3, 2, 1, 0, 7, 6, 5, 4},
              {4, 5, 6, 7, 0, 1, 2, 3},
              {5, 4, 7, 6, 1, 0, 3, 2},
              {6, 7, 4, 5, 2, 3, 0, 1},
              {7, 6, 5, 4, 3, 2, 1, 0}},
          new int[][] {{0, 0, 0, 0, 0, 0, 0, 0},
              {0, 1, 2, 3, 4, 5, 6, 7},
              {0, 2, 4, 6, 3, 1, 7, 5},
              {0, 3, 6, 5, 7, 4, 1, 2},
              {0, 4, 3, 7, 6, 2, 5, 1},
              {0, 5, 1, 4, 2, 7, 3, 6},
              {0, 6, 7, 1, 5, 3, 2, 4},
              {0, 7, 5, 2, 1, 6, 4, 3}})};

  private FiniteFieldTable table;

  private SavedFiniteField(FiniteFieldTable table) {
    this.table = table;
  }

  /**
   * Returns a FiniteField for the given size, if a saved Field is present.
   * 
   * @param size The required size of the field.
   * @return The saved field if present.
   * @throws IllegalArgumentException if no finite field is saved for the given value.
   */
  public static FiniteField getSavedFiniteField(int size) {
    for (FiniteFieldTable table : finiteFields) {
      if (table.size == size) {
        return new SavedFiniteField(table);
      }
    }
    throw new IllegalArgumentException("No FiniteField found for given size: " + size);
  }

  @Override
  public int add(int a, int b) {
    return table.additionTable[a][b];
  }

  @Override
  public int subtract(int a, int b) {
    return table.additionTable[a][addInvers(b)];
  }

  @Override
  public int multiply(int a, int b) {
    return table.multiplicationTable[a][b];
  }

  @Override
  public int divide(int a, int b) {
    return table.multiplicationTable[a][multInvers(b)];

  }

  @Override
  public int addInvers(int a) {
    return a;
  }

  @Override
  public int multInvers(int b) {
    if (b == 0) {
      throw new ArithmeticException("No multiplicative invers exists for zero");
    }
    int[] multA = table.multiplicationTable[b];
    for (int i = 0; i < multA.length; i++) {
      if (multA[i] == 1) {
        return i;
      }
    }

    throw new ArithmeticException("No multiplicative invers found for number " + b);
  }

  @Override
  public int size() {
    return table.size;
  }

  private static class FiniteFieldTable {

    private int size;

    private int[][] additionTable;

    private int[][] multiplicationTable;

    public FiniteFieldTable(int size, int[][] additionTable, int[][] multiplicationTable) {
      this.size = size;
      this.additionTable = additionTable;
      this.multiplicationTable = multiplicationTable;
    }
  }

}

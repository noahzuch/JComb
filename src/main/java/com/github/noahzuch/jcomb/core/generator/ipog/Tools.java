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

/**
 * A Tools class for the IPOG algorithm
 * @author Noah
 *
 */
public final class Tools {

  private Tools() {

  }

  /**
   * Converts the given value combination array into its index representation.
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param combination The value combination array to convert. Has to be at lest of size
   *        max{parameters}.
   * @param parameters The parameters in combination to take into account, when creating the index.
   * @return The index of the value combination.
   */
  public static int packValues(int[] domainSizes, int[] combination, int[] parameters) {
    int index = combination[parameters[0]];
    for (int i = 1; i < parameters.length; i++) {
      int parameterIndex = parameters[i];
      index = index * domainSizes[parameterIndex] + combination[parameterIndex];
    }
    return index;
  }

  /**
   * Converts the given value combination array into its index representation. But leaves out the
   * last parameter and its value.
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param combination The value combination array to convert. Has to be at lest of size
   *        max{parameters}.
   * @param parameters The parameters in combination to take into account, when creating the index.
   *        The last parameter in this array is ignored.
   * @return The index of the value combination.
   */
  public static int packValuesExceptLast(int[] domainSizes, int[] combination, int[] parameters) {
    int index = combination[parameters[0]];
    for (int i = 1; i < parameters.length - 1; i++) {
      int parameterIndex = parameters[i];
      index = index * domainSizes[parameterIndex] + combination[parameterIndex];
    }
    return index;
  }

  /**
   * Converts a given value index to his corresonding value array representation.
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param index The value index to convert.
   * @param parameterCount The total amount of parameters.
   * @param parameters The parameters used in this value index.
   * @return An array of size parameterCount containing
   */
  public static int[] unpackValues(int[] domainSizes, int index, int parameterCount,
      int[] parameters) {
    int[] combination = new int[parameterCount];
    fillWithParameterValues(domainSizes, index, parameters, combination);
    return combination;
  }

  /**
   * Creates a new test with values of the tupel defined by index and an ascending order of
   * parameters. Every other value is -1.
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param index The index representation of the tupel.
   * @param parameterOffset The first parameter to fill
   * @param parameterCount The amount of parameters to fill
   * @return a new test as an int array.
   */
  public static int[] generateNewTest(int[] domainSizes, int index, int parameterOffset,
      int parameterCount) {
    int[] test = new int[domainSizes.length];
    Arrays.fill(test, -1);
    fillWithParameterValues(domainSizes, index, parameterOffset, parameterCount, test);
    return test;
  }

  /**
   * Creates a new test with the values of the tupel defined by index, and every other value being
   * -1.
   * 
   * @param domainSizes The sizes of the parameter domains
   * @param index The index representation of the tupel.
   * @param paramComb The combination of parameters for the given value index
   * @return a new test as an int array.
   */
  public static int[] generateNewTest(int[] domainSizes, int index, int[] paramComb) {
    int[] test = new int[domainSizes.length];
    Arrays.fill(test, -1);
    fillWithParameterValues(domainSizes, index, paramComb, test);
    return test;
  }

  /**
   * Fills the given test with values of the tupel defined by index and an ascending order of
   * parameters. This method is an easy way of calling:
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param index The index representation of the tupel.
   * @param parameterOffset The first parameter to fill
   * @param parameterCount The amount of parameters to fill
   * @param testToFill the test to fill.
   */
  public static void fillWithParameterValues(int[] domainSizes, int index, int parameterOffset,
      int parameterCount, int[] testToFill) {
    for (int i = parameterCount - 1; i >= 0; i--) {
      int paramDomainSize = domainSizes[i + parameterOffset];
      int x = index % paramDomainSize;
      testToFill[parameterOffset + i] = x;
      index = (index - x) / paramDomainSize;
    }
  }

  /**
   * Fills the given test with values of the tupel defined by index.
   * 
   * @param domainSizes The sizes of the parameter domains.
   * @param index The index representation of the tupel
   * @param paramComb The combination of parameters for the given value index
   * @param testToFill The test to fill in values.
   */
  public static void fillWithParameterValues(int[] domainSizes, int index, int[] paramComb,
      int[] testToFill) {
    for (int i = paramComb.length - 1; i >= 0; i--) {
      int paramIndex = paramComb[i];
      int paramDomainSize = domainSizes[paramIndex];
      int x = index % paramDomainSize;
      testToFill[paramIndex] = x;
      index = (index - x) / paramDomainSize;
    }
  }

  /**
   * Calculates the binomial coefficient n choose k.
   * 
   * @param n First parameter.
   * @param k Second parameter.
   * @return the binomal coefficient.
   */
  public static int binomCoeff(int n, int k) {
    if (k <= 0 || k >= n) {
      return 1;
    }
    if (k > n / 2) {
      return binomCoeff(n, n - k);
    }
    float res = 1;
    for (int i = 0; i < k; i++) {
      res *= n - i;
      res /= i + 1;
    }
    return (int) res;
  }

  /**
   * Returns the parameter combination with index 0 for iterating over every combination.
   * 
   * @param strength The strength of the combination aka the strength of the combinatorial test.
   * @return An int[] representing the parameter combination with index 0.
   */
  public static int[] getFirstParamComb(int strength) {
    int[] first = new int[strength];
    for (int i = 0; i < first.length; i++) {
      first[i] = i;
    }
    return first;
  }

  /**
   * Updates the given parameter combination with any index i to be the combination with index i+1.
   * Does not wrap around from the last index to index 0, but creates wrong combinations! Only use
   * it for combinations with index smaller than the biggest index.
   * 
   * @param currentComb The parameter combination to update to the next index.
   */
  public static void getNextParamComb(int[] currentComb) {
    int lastParam = currentComb[0];
    int adjacentParamCount = 0;
    for (int i = 1; i < currentComb.length; i++) {
      if (currentComb[i] == lastParam + 1) {
        lastParam = currentComb[i];
        adjacentParamCount++;
        if (i == currentComb.length - 1) {
          currentComb[i]++;
        }
      } else {
        currentComb[i - 1] += 1;
        break;
      }
    }
    for (int i = 0; i < adjacentParamCount; i++) {
      currentComb[i] = i;
    }
  }

  /**
   * Returns the parameter combination with index 0 for iterating over every combination where the
   * last parameter (parameterCount-1) is fix.
   * 
   * @param strength The strength of the combination aka the strength of the combinatorial test.
   * @param parameterCount The complete amount of parameters.
   * @return An int[] representing the parameter combination with index 0.
   */
  public static int[] getFirstParamCombWithFixParam(int strength, int parameterCount) {
    int[] first = getFirstParamComb(strength);
    first[strength - 1] = parameterCount - 1;
    return first;
  }

  /**
   * Updates the given parameter combination with fix last parameter with any index i to be the
   * combination with index i+1. Does not wrap around from the last index to index 0, but creates
   * wrong combinations! Only use it for combinations with index smaller than the biggest index.
   * 
   * @param currentComb The parameter combination to update to the next index.
   */
  public static void getNextParamCombWithFixParam(int[] currentComb) {
    int lastParam = currentComb[0];
    int adjacentParamCount = 0;
    for (int i = 1; i < currentComb.length; i++) {
      if (currentComb[i] == lastParam + 1) {
        lastParam = currentComb[i];
        adjacentParamCount++;
        if (i == currentComb.length - 2) {
          currentComb[i] += 1;
          break;
        }
      } else {
        currentComb[i - 1] += 1;
        break;
      }
    }
    for (int i = 0; i < adjacentParamCount; i++) {
      currentComb[i] = i;
    }
  }
}

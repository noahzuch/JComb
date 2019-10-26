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
import com.github.noahzuch.jcomb.core.oa.finitefield.FiniteField;
import com.github.noahzuch.jcomb.core.oa.finitefield.PrimeField;
import com.github.noahzuch.jcomb.core.oa.finitefield.SavedFiniteField;

/**
 * A utility class for orthogonal arrays.
 * 
 * @author Noah Zuch
 *
 */
public class Tools {

  private Tools() {

  }

  /**
   * Checks if a given number is a prime number.
   * 
   * @param n the number to check.
   * @return True if the given nubmer is prime, false otherwise
   */
  public static boolean isPrime(int n) {
    if (n < 2) {
      return false;
    }
    if (n == 2 || n == 3) {
      return true;
    }
    if (n % 2 == 0 || n % 3 == 0) {
      return false;
    }
    int sqrtN = (int) Math.sqrt(n) + 1;
    for (int i = 6; i <= sqrtN; i += 6) {
      if (n % (i - 1) == 0 || n % (i + 1) == 0) {
        return false;
      }
    }
    return true;
  }



  /**
   * Returns a list of all finite fields needed for the OA algorithm.
   * 
   * @param n The number of values in the OA algorithm.
   * @return A List of all finite fields needed.
   */
  public static List<FiniteField> getFiniteFieldsViaPrimeFactors(int n) {
    List<FiniteField> factors = new ArrayList<>();
    for (int i = 2; i <= n / i; i++) {
      if (n % i == 0) {
        int prime = i;
        int primePower = 1;
        int power = 0;
        while (n % i == 0) {
          power++;
          primePower *= prime;
          n /= i;
        }
        if (power == 1) {
          factors.add(new PrimeField(prime));
        } else {
          factors.add(SavedFiniteField.getSavedFiniteField(primePower));
        }
      }
    }
    if (n > 1) {
      factors.add(new PrimeField(n));
    }
    return factors;
  }

}

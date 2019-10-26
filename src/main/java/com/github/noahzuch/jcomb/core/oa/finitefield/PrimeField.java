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

import com.github.noahzuch.jcomb.core.generator.oa.Tools;

/**
 * A FiniteField where the number of values is a prime.
 * 
 * @author Noah Zuch
 *
 */
public class PrimeField implements FiniteField {

  int prime;

  /**
   * Creates a new prime field with the given number of values.
   * 
   * @param prime the size of the field. Has to be a prime number.
   */
  public PrimeField(int prime) {
    if (!Tools.isPrime(prime)) {
      throw new IllegalArgumentException("supplied number is not a prime");
    }
    this.prime = prime;
  }

  @Override
  public int subtract(int a, int b) {
    return (a + addInvers(b)) % prime;
  }

  @Override
  public int multiply(int a, int b) {
    return (a * b) % prime;
  }

  @Override
  public int multInvers(int a) {
    if (a == 0) {
      throw new ArithmeticException("No multiplicative invers exists for zero");
    }
    int inv = 1;
    for (int i = 0; i < prime - 2; i++) {
      inv = (inv * a) % prime;
    }
    return inv;
  }

  @Override
  public int divide(int a, int b) {
    return (a * multInvers(b)) % prime;
  }

  @Override
  public int addInvers(int a) {
    return prime - a;
  }

  @Override
  public int add(int a, int b) {
    return (a + b) % prime;
  }

  @Override
  public int size() {
    return prime;
  }
}

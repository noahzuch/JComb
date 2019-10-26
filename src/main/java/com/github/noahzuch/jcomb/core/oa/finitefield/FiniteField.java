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
 * This class represents a finite field in mathematics. Numbers are represented as ints ranging from
 * zero to the size of the field minus one. The value zero is the identity for addition and the
 * value one for multiplication.
 * 
 * @author Noah Zuch
 *
 */
public interface FiniteField {

  /**
   * Returns the number of values in this FiniteField. The calculation methods can be used with
   * numbers from 0 to size()-1.
   * 
   * @return The size of this finite field.
   */
  int size();

  /**
   * Adds two values together.
   * 
   * @param a The first value.
   * @param b The second value.
   * @return The result of the addition
   */
  int add(int a, int b);

  /**
   * Subtracts the second from the first value.
   * 
   * @param a The value to subtract from.
   * @param b The value to subtract.
   * @return The result of the subtraction.
   */
  int subtract(int a, int b);

  /**
   * Multiplies two values together.
   * 
   * @param a The first value.
   * @param b The second value.
   * @return The result of the multiplication
   */
  int multiply(int a, int b);

  /**
   * Divides the first values by the second value.
   * 
   * @param a The value to divide.
   * @param b The value to divide by.
   * @return The result of the division.
   */
  int divide(int a, int b);

  /**
   * Returns the additive inverse of the supplied number.
   * 
   * @param a The number to get the inverse from.
   * @return The inverse in respect to addition.
   */
  int addInvers(int a);

  /**
   * Returns the multiplicative inverse of the supplied number.
   * 
   * @param b The number to get the inverse from.
   * @return The inverse in respect to multiplication.
   */
  int multInvers(int b);

}

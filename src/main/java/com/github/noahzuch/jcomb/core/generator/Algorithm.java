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
package com.github.noahzuch.jcomb.core.generator;

/**
 * An enum for all the possible algorithms supported by JComb.
 * 
 * @author Noah Zuch
 *
 */
public enum Algorithm {
  /**
   * This value is the default for a combinatorial test. One of the supported algorithms gets
   * selected automatically based on the given parameters and constraints.
   */
  ANY,
  /**
   * The Orthogonal Array (OA) algorithm is the fastest one and creates the smallest test set
   * possible. But it can only be used if the following criterias are met: Strength has to be 2.
   * There are no defined constraints. Every Parameter has to have the same number of possible
   * values. For a given value count v and its primefactorisation p_1^e_1*...*P_n^e_n the number of
   * parameters has to be below min{p_i^e_i}+2. Only true primes and the primepowers 4, 8 and 9 are
   * currently allowed in the factorisation.
   */
  OA,
  /**
   * In Parameter Order Generalized (IPOG) is the slower of the two algorithms and does not always
   * create the best test set possible. But it has none of the limitations of the OA algorithm an
   * can be executed with any inputs.
   */
  IPOG
}

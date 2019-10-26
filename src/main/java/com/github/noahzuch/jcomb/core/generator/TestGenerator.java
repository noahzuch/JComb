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

import java.util.stream.Stream;

/**
 * A TestInputGenerator is used for generating all relevant input combinations.
 * 
 * @author Noah Zuch
 *
 */
public interface TestGenerator {

  /**
   * Generates all relevant input combinations as Stream of int arrays. One int array represents a
   * single test input combination.
   * 
   * @return A stream of input combinations represented as an int array.
   */
  Stream<int[]> getAllInputCombinations();

}

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
package com.github.noahzuch.jcomb.core.constraint;

/**
 * A constraint handler can check if a (partial) test is or can satisfy all constraints in the
 * constraint handler.
 * 
 * @author Noah Zuch
 *
 */
public interface ConstraintHandler {

  /**
   * Checks if a given test is satisfiable under this ConstraintHandler.
   * @param test The test to check. A value of -1 indicates a wildcard value.
   * @return Whether or not the supplied test is satisviable.
   */
  public boolean isSatisfiable(int[] test);

  /**
   * Returns the default {@link ConstraintHandler}, which allows all tests.
   * 
   * @return The default ConstraintHandler.
   */
  public static ConstraintHandler getDefaultConstraintHandler() {
    return new ConstraintHandler() {

      @Override
      public boolean isSatisfiable(int[] test) {
        return true;
      }
    };
  }
}

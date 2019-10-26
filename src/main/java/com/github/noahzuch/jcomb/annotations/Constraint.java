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
package com.github.noahzuch.jcomb.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be used to define Constraints for the combinatorial test. A Constraint is a
 * static method, that returns a boolean.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * &#64;Parameter(0)
 * private static Ints parameter1 = new Ints(1, 2, 3);
 * 
 * &#64;Parameter(1)
 * private static Values parameter2 = new Values("1", "2", "3");
 * 
 * &#64;Constraint(id=0, parameters={0,1})
 * public static boolean checkNotEqual(int param1, Strint param2){
 *     return param1 != Integer.parseInt(param2);
 * }
 * </pre>
 * 
 * @author Noah
 *
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Constraint {

  /**
   * The id of the constraint.
   * 
   * @return The id of the constraint.
   */
  int id();

  /**
   * The parameters required for this constraint. The annotated method has to define its parameters
   * in the same order.
   * 
   * @return The parameters required for this constraint.
   */
  int[] parameters();
}

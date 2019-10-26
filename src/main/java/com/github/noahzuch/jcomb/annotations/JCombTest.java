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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.TestTemplate;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

/**
 * This annotation has to be used instead of the normal {@link org.junit.jupiter.api.Test}
 * annotation from JUnit when the test method should use JComb. By default every defined parameter
 * and constraint is used for the test. The test method has to define its parameters in the same
 * order as given by the parameters indices. If a subset of the parameters is needed, the
 * {@link #parameters()} field can be used. This overwrites the default ordering!
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * &#64;Parameter(index = 0)
 * private static Values parameter1 = new Values(1,2,3,4);
 * 
 * &#64;Parameter(index = 1)
 * private static Values parameter2 = new Values("s1","s2,"s3");
 * 
 * &#64;JCombTest
 * void testMethod(int param1, String param2){
 *  ...
 * }
 * 
 * &#64;JCombTest(parameters = {2,1})
 * void testMethod2(String param1, int param2){
 *  ...
 * }
 * 
 * &#64;JCombTest(parameters = {1})
 * void testMethod2(int param){
 *  ...
 * }
 * </pre>
 * 
 * @author Noah Zuch
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@TestTemplate
public @interface JCombTest {

  /**
   * A specific algorithm can be set, that should be used for the combinatorial test generation.
   * Some algorithms need preconditions to run successfully. See the documentation in
   * {@link Algorithm} for more information about each algorithm. The default value is
   * {@link Algorithm#ANY}.
   * 
   * @return The algorithm to use for test generation
   */
  Algorithm algorithm() default Algorithm.ANY;

  /**
   * If only a subset of parameters should be used for this test method, they can be defined here as
   * an array of there indices. The order of the array overwrites the natural order of the
   * parameters and the test method has to match the here defined order! By default every defined
   * Parameter is used in the order of there indices.
   * 
   * @return the parameters to use test generation.
   */
  int[] parameters() default {};

  /**
   * If only a subset of constraints should be used for this test method, they can be defined here
   * as an array of there ids.
   * 
   * @return
   */
  int[] constraints() default {};

  /**
   * Defines the strength of the combinatorial test. Default is 2.
   * 
   * @return
   */
  int strength() default 2;

  /**
   * If no constraint should be used set ignoreConstraints to <code>true</code>.
   * 
   * @return
   */
  boolean ignoreConstraints() default false;
}

/**
 * Copyright 2019 Noah Zuch noahz97@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.noahzuch.jcomb.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use this annotation for a parameter in the testmethod to get the value of the defined parameter
 * as the index and not as the concrete value. The annotated parameter has to be of type {@link int}
 * or {@link Integer}
 * 
 * <pre>
 * &#64;Parameter(index = 0)
 * private static Values parameter0 = new Values(1,2,3,4);
 * 
 * &#64;Parameter(index = 1)
 * private static Values parameter1 = new Values("s1","s2,"s3");
 * 
 * &#64;JCombTest
 * void testMethod(int param0, String param1, @ValueIndex(1)int indexOfParam1){
 *  ...
 * }
 * </pre>
 * 
 * @author Noah
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValueIndex {

  /**
   * The parameter id to get the index from.
   * 
   * @return
   */
  int value();
}

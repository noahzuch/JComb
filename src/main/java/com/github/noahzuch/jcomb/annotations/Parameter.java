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
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.google.common.primitives.Longs;

/**
 * With this annotations parameters of a combinatorial test can be defined. The type of the
 * annotated variable or the method return type has to be a subclass of {@link Domain}. The class
 * {@link Values} and classes for primitives (e.g. {@link Ints} or {@link Longs}) are the standard
 * ways to define a Parameter, but custom subclasses can be used aswell. A parameter can be defined
 * in the following ways:
 * 
 * <p>
 * via a Field:
 * 
 * <pre>
 * &#64;Parameter(0)
 * private Values parameter1 = new Values(1,2,3,4, ...);
 * </pre>
 * 
 * <p>
 * or via a method:
 * 
 * <pre>
 * &#64;Parameter(0)
 * private static Values getParameter1(){
 *     return new Values(1,2,3,4, ...);
 * }
 * </pre>
 * 
 * <p>
 * Note, that the Method/Field can be defined static or non-static. A non static parameter gets
 * reinitialized for every single test execution, a static one only once for the whole combinatorial
 * test.
 * 
 * <p>
 * A parameter has to be defined with an index. The index can be used later to refer to this
 * specific Parameter. Therefore the index has to be unique for all parameters in the current class
 * and any inherited classes (see {@link InheritSetup}). For a better overview of the defined
 * parameters in bigger combinatorial test setups it is recomended to use an int constant for every
 * index:
 * 
 * <pre>
 * public static final int PARAMETER_1 = 0;
 * &#64;Parameter(PARAMETER_1)
 * private Values parameter1 = new Values(1,2,3,4, ...);
 * </pre>
 * 
 * @author Noah Zuch
 *
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Parameter {

  /**
   * A {@code Parameter} has to be defined with an index. The index can be used later to refer to
   * this specific Parameter. Therefore the index has to be unique for all parameters in the current
   * test setup.
   * 
   * @return an integer greater or equal 0 used as the index of the parameter.
   */
  int value();
}

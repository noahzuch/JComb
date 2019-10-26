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
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be used if the parameters and constraints in a different class should be
 * included in another testclass. To use this annotation an instantiated nonstatic field of the
 * class to inherit from has to be annotated in the testclass. Cyclic inheritance is ignored. Also a
 * subclass always inherits the parameters and constraints from its parent class and no annotation
 * is needed.
 * 
 * <pre>
 * public class ClassThatInherits {
 * 
 *   &#64;InheritSetup
 *   private ClassToInheritFrom object = new ClassToInheritFrom();
 * 
 * }
 * </pre>
 * 
 * @author Noah
 *
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD})
public @interface InheritSetup {
}

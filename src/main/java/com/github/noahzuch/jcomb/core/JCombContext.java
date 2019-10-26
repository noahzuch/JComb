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

package com.github.noahzuch.jcomb.core;

import java.util.Map;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

/**
 * Holds information for the current combinatorial test.
 * 
 * @author Noah Zuch
 *
 */
public interface JCombContext extends InstanceDependent {
  /**
   * Retrieves a {@link Map} of all the Constraints, that are defined for the current context.
   * 
   * @return A Collection of relevant {@link Constraint}s.
   */
  public Map<Integer, Constraint> getConstraints();

  /**
   * Retrieves the {@link Domain} object for a given index. This index IS NOT the defined index in
   * the {@link JCombTest} annotation, but the index of the parameter of the test method. See
   * {@link JCombTest#parameters()} for more info.
   * 
   * @param index The index of the input parameter.
   * @return The corresponding parameter object.
   */
  public Domain getParameter(int index);

  /**
   * Retrieves the number of parameters for the current test.
   * 
   * @return The number of parameters relevant for the current test.
   */
  public int getParameterCount();

  /**
   * Returns the strength of this combinatorial test.
   * 
   * @return The strength of this combinatorial test.
   */
  public int getStrength();

  /**
   * Returns the used algorithm of this combinatorial test.
   * 
   * @return The used algorithm of this combinatorial test.
   */
  public Algorithm getAlgorithm();

}

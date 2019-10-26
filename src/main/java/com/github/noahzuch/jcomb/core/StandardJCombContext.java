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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.noahzuch.jcomb.annotations.AnnotationFinder;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

/**
 * The implementation of a JCombContext created by the {@link AnnotationFinder}.
 * 
 * @author Noah Zuch
 *
 */
public class StandardJCombContext implements JCombContext {

  private List<Domain> parameters;
  private List<InstanceDependent> instanceDependant;
  private Map<Integer, Constraint> constraints;

  /**
   * Creates a new {@link StandardJCombContext}.
   */
  public StandardJCombContext() {
    parameters = new ArrayList<>();
    constraints = new HashMap<>();
    instanceDependant = new ArrayList<>();
  }

  @Override
  public Map<Integer, Constraint> getConstraints() {
    return constraints;
  }

  @Override
  public Domain getParameter(int index) {
    return parameters.get(index);
  }

  @Override
  public int getParameterCount() {
    return parameters.size();
  }

  @Override
  public int getStrength() {
    return 2;
  }

  /**
   * Sets the constraints for this JCombContext.
   * @param constraints The constraints as a map with their ids.
   */
  public void setConstraints(Map<Integer, Constraint> constraints) {
    this.constraints = constraints;
  }

  /**
   * Returns the constraints of this JCombContext.
   * @return The constraints of this JCombContext.
   */
  public Map<Integer, Constraint> getConstraintsMap() {
    return constraints;
  }

  @Override
  public void initializeForInstance(InstanceInformation instanceInformation) {
    instanceDependant.forEach(obj -> obj.initializeForInstance(instanceInformation));
  }

  /**
   * Adds the given parameters to the parameter list of this context.
   * 
   * @param parameters The parameters to add.
   */
  public void addParameters(List<Domain> parameters) {
    this.parameters.addAll(parameters);
    parameters.stream()
        .filter(obj -> obj instanceof InstanceDependent)
        .map(obj -> (InstanceDependent) obj).forEach(instanceDependant::add);
  }

  @Override
  public Algorithm getAlgorithm() {
    return Algorithm.ANY;
  }

}

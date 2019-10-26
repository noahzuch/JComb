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

package com.github.noahzuch.jcomb.core.partial;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.HashMap;
import java.util.Map;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

/**
 * This class can be used if for a given test method not the whole {@link JCombContext} of the test
 * class should be used. This class then wraps the existing {@link JCombContext} object and makes
 * only specific parameters, constraints and seeds visible.
 * 
 * @author Noah Zuch
 *
 */
public class PartialJCombContext implements JCombContext {

  private JCombContext context;
  private ParameterReorder parameterReorder;
  private Map<Integer, Constraint> constraints;
  private int strength;
  private Algorithm algorithm;

  /**
   * Creates a new PartialJCombContext for the given information.
   * @param algorithm The new algorithm to use.
   * @param strength The new strength to use.
   * @param parameterMapping The new parameter mapping as an array.
   * @param relevantConstrIndizes An array of the relevant constraints for this partial context.
   * @param context The old context to use for the partial one.
   */
  public PartialJCombContext(Algorithm algorithm, int strength, int[] parameterMapping,
      int[] relevantConstrIndizes,
      JCombContext context) {
    checkNotNull(context);
    this.context = context;

    checkNotNull(algorithm);
    checkNotNull(parameterMapping);
    checkNotNull(relevantConstrIndizes);
    checkParameterMapping(parameterMapping);
    if (strength < 1) {
      throw new JCombException("The strength of a combinatorial test has to be at least 1");
    }
    if (strength > parameterMapping.length) {
      throw new JCombException(
          "The strength of a combinatorial test can't be greated than the number of parameters");
    }
    this.strength = strength;
    this.algorithm = algorithm;
    parameterReorder = new ParameterReorder(parameterMapping, context.getParameterCount());
    checkAndSetupConstraints(relevantConstrIndizes);
  }

  private void checkParameterMapping(int[] parameterMapping) {
    for (int i = 0; i < parameterMapping.length; i++) {
      int mapping = parameterMapping[i];
      if (mapping < 0 || mapping >= context.getParameterCount()) {
        throw new JCombException("The testmethod requires a parameter with id '" + mapping
            + "', but no Parameter with such an id is defined");
      }
      for (int j = 0; j < parameterMapping.length; j++) {
        if (j != i && parameterMapping[j] == mapping) {
          throw new JCombException(
              "The parameter '" + mapping
                  + "' is used twice for the testmethod. As of now defining a parameter twice in a "
                  + "method is not allowed.");
        }
      }
    }
  }

  private void checkAndSetupConstraints(int[] relevantConstraints) {
    constraints = new HashMap<>();
    for (int i = 0; i < relevantConstraints.length; i++) {
      Constraint constraint = context.getConstraints().get(relevantConstraints[i]);
      if (constraint != null) {
        Constraint reorderedConstraint = ReorderedConstraint
            .getReorderedConstraintFromPartialContext(constraint, parameterReorder);
        constraints.put(relevantConstraints[i], reorderedConstraint);

      } else {
        throw new JCombException(
            "The testmethod requires a constraint with id '" + relevantConstraints[i]
                + "', but no Constraint with such an id is defined");
      }
    }
  }

  @Override
  public void initializeForInstance(InstanceInformation instanceInformation) {
    context.initializeForInstance(instanceInformation);
  }

  @Override
  public Domain getParameter(int index) {
    checkIndex(index);
    return context.getParameter(parameterReorder.getOldParamIndexFromNew(index));
  }

  @Override
  public int getParameterCount() {
    return parameterReorder.getReorderedParameterCount();
  }

  private void checkIndex(int index) {
    if (!parameterReorder.isLegalNewParam(index)) {
      throw new IllegalArgumentException(
          "supplied index is not is illegal parameter.");
    }
  }

  @Override
  public int getStrength() {
    return strength;
  }

  @Override
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  @Override
  public Map<Integer, Constraint> getConstraints() {
    return constraints;
  }

}

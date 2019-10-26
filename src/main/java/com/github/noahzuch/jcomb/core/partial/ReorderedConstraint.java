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

import com.github.noahzuch.jcomb.core.constraint.Constraint;

/**
 * A Constraint that is based on an already existing one, but uses indices from a ParameterReorder
 * instead of the original ones.
 * 
 * @author Noah
 *
 */
public class ReorderedConstraint implements Constraint {

  private Constraint wrappedConstraint;

  private int[] parameterReOrder;

  private ReorderedConstraint(Constraint wrappedConstraint, int[] parameterReOrder) {
    this.wrappedConstraint = wrappedConstraint;
    this.parameterReOrder = parameterReOrder;
  }

  /**
   * Creates (if possible) a ReorderedConstraint object from a given constraint and a
   * {@link ParameterReorder} object. If not all the neccesary parameters for the constraint are
   * included in the ParameterReorder, no ReorderedConstraint ca be created and null is returned
   * instead.
   * 
   * @param standard The constraint to base the new ReorderedConstraint on.
   * @param parameterReorder The {@link ParameterReorder} to use for the reordering.
   * @return A ReorderedConstraint based on the original constraint or null if the constraint could
   *         not be reordered.
   */
  public static ReorderedConstraint getReorderedConstraintFromPartialContext(Constraint standard,
      ParameterReorder parameterReorder) {
    int[] oldInvolvedParameters = standard.getInvolvedParameters();
    int[] reorderedParameters = new int[oldInvolvedParameters.length];
    for (int i = 0; i < oldInvolvedParameters.length; i++) {
      int oldParam = oldInvolvedParameters[i];
      if (parameterReorder.containsReorderForOldParam(oldParam)) {
        reorderedParameters[i] = parameterReorder.getNewParamIndexFromOld(oldParam);
      } else {
        return null;
      }
    }
    return new ReorderedConstraint(standard, reorderedParameters);
  }

  @Override
  public int[] getInvolvedParameters() {
    return parameterReOrder;
  }

  @Override
  public boolean confirmsWith(Object[] inputKombination) {
    return wrappedConstraint.confirmsWith(inputKombination);
  }

}

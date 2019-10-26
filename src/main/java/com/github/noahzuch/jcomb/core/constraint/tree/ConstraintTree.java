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

package com.github.noahzuch.jcomb.core.constraint.tree;

import java.util.List;
import java.util.stream.Collectors;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.domain.Domain;

public class ConstraintTree implements ConstraintHandler {

  private CheckNode root;
  private static final CheckNode falseLeave = new LeaveNode(false);

  /**
   * Creates a new ConstraintTree for the given context.
   * 
   * @param jcombContext The context to create a ConstraintTree from.
   */
  public ConstraintTree(JCombContext jcombContext) {
    List<Constraint> constraints = jcombContext.getConstraints().values().stream()
        .sorted((l1, l2) -> Integer.compare(l1.getInvolvedParameters().length,
            l2.getInvolvedParameters().length))
        .collect(Collectors.toList());
    if (constraints.size() == 0) {
      root = null;
    } else {
      root = createNode(jcombContext, new Object[jcombContext.getParameterCount()], 0,
          new boolean[jcombContext.getParameterCount()], 0, constraints);
    }
  }

  @Override
  public boolean isSatisfiable(int[] combination) {
    // null represents true, but decreases tree size
    return root == null || root.isSatisfiable(combination);
  }

  private CheckNode createNode(JCombContext context, Object[] combination,
      int currentConstraintParameter,
      boolean[] coveredParameters, int currentConstraint,
      List<Constraint> constraintIt) {
    int currentParameter =
        constraintIt.get(currentConstraint).getInvolvedParameters()[currentConstraintParameter];
    // check whether or not this is the last parameter for the current constraint
    if (currentConstraintParameter == constraintIt.get(currentConstraint)
        .getInvolvedParameters().length
        - 1) {
      if (!coveredParameters[currentParameter]) {
        // if the last parameter is not yet covered, cover it and at the same time check for the
        // constraint
        coveredParameters[currentParameter] = true;
        CheckNode node =
            createConstraintEndNode(context, combination, currentConstraintParameter,
                coveredParameters,
                currentConstraint,
                constraintIt);
        coveredParameters[currentParameter] = false;
        return node;
      } else {
        // if the last parameter is already covered, only check for the constraint
        return checkConstraintEndNode(context, combination, currentConstraintParameter,
            coveredParameters,
            currentConstraint,
            constraintIt);
      }
    } else {
      if (!coveredParameters[currentParameter]) {
        // if the current parameter is not yet covered, cover it
        coveredParameters[currentParameter] = true;
        CheckNode node =
            createCompleteInnerNode(context, combination, currentConstraintParameter,
                coveredParameters,
                currentConstraint, constraintIt);
        coveredParameters[currentParameter] = false;
        return node;
      } else {
        // if the current parameter is already covered, go on with the next parameter
        return createNode(context, combination, currentConstraintParameter + 1, coveredParameters,
            currentConstraint,
            constraintIt);
      }
    }
  }

  private CheckNode checkConstraintEndNode(JCombContext context, Object[] combination,
      int currentConstraintParameter,
      boolean[] coveredParameters, int currentConstraint,
      List<Constraint> constraintIt) {
    Constraint constraint = constraintIt.get(currentConstraint);

    if (!checkConstraintWithFullCombination(constraint, combination)) {
      // Constraint not fulfilled
      return falseLeave;
    } else {
      // Constraint fulfilled
      if (currentConstraint < constraintIt.size() - 1) {
        return createNode(context, combination, 0, coveredParameters, currentConstraint + 1,
            constraintIt);
      } else {
        return null; // null represents true, but decreases tree size
      }
    }
  }

  private CheckNode createConstraintEndNode(JCombContext context, Object[] combination,
      int currentConstraintParameter,
      boolean[] coveredParameters, int currentConstraint,
      List<Constraint> constraintIt) {
    Constraint constraint = constraintIt.get(currentConstraint);
    int currentParameter = constraint.getInvolvedParameters()[currentConstraintParameter];
    Domain parameter = context.getParameter(currentParameter);
    InnerNode newNode = null;

    for (int valueIndex = 0; valueIndex < parameter.getSize(); valueIndex++) {
      combination[currentParameter] = parameter.getValueAt(valueIndex);
      if (!checkConstraintWithFullCombination(constraint, combination)) {
        // Constraint not fulfilled
        if (newNode == null) {
          newNode = new InnerNode(currentParameter, parameter.getSize());
        }
        newNode.setChild(valueIndex, falseLeave);
      } else {
        // Constraint fulfilled
        CheckNode child;
        if (currentConstraint < constraintIt.size() - 1) {
          child =
              createNode(context, combination, 0, coveredParameters, currentConstraint + 1,
                  constraintIt);
        } else {
          child = null; // null represents true, but decreases tree size
        }
        if (child != null) {
          if (newNode == null) {
            newNode = new InnerNode(currentParameter, parameter.getSize());
          }
          newNode.setChild(valueIndex, child);
        }
      }
    }
    return newNode;
  }

  private CheckNode createCompleteInnerNode(JCombContext context, Object[] combination,
      int currentConstraintParameter,
      boolean[] coveredParameters, int currentConstraint,
      List<Constraint> constraintIt) {
    Constraint constraint = constraintIt.get(currentConstraint);
    int currentParameter = constraint.getInvolvedParameters()[currentConstraintParameter];
    InnerNode newNode = null;
    Domain parameter = context.getParameter(currentParameter);
    for (int valueIndex = 0; valueIndex < parameter.getSize(); valueIndex++) {
      combination[currentParameter] = parameter.getValueAt(valueIndex);
      CheckNode child =
          createNode(context, combination, currentConstraintParameter + 1, coveredParameters,
              currentConstraint,
              constraintIt);
      if (child != null) {
        if (newNode == null) {
          newNode = new InnerNode(currentParameter, parameter.getSize());
        }
        newNode.setChild(valueIndex, child);
      }
    }
    return newNode;
  }

  private boolean checkConstraintWithFullCombination(Constraint con, Object[] combination) {
    Object[] input = new Object[con.getInvolvedParameters().length];
    for (int i = 0; i < con.getInvolvedParameters().length; i++) {
      input[i] = combination[con.getInvolvedParameters()[i]];
    }
    return con.confirmsWith(input);
  }

}

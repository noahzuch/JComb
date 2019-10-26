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

public class InnerNode implements CheckNode {

  private int parameterIndex;
  private CheckNode[] children;

  public InnerNode(int parameterIndex, int childCount) {
    this.parameterIndex = parameterIndex;
    this.children = new CheckNode[childCount];
  }

  public void setChild(int valueIndex, CheckNode node) {
    children[valueIndex] = node;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSatisfiable(int[] combination) {
    if (combination[parameterIndex] == -1) {
      // This node is not important. propagate call to all children
      return checkAllChildren(combination);
    } else {
      CheckNode child = children[combination[parameterIndex]];
      if (child == null) {
        return true;
      } else {
        return child.isSatisfiable(combination);
      }
    }
  }

  private boolean checkAllChildren(int[] combination) {
    for (CheckNode child : children) {
      if (child == null || child.isSatisfiable(combination)) {
        return true;
      }
    }
    return false;
  }
}

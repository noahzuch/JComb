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

public class LeaveNode implements CheckNode {

  private boolean isSatisviable;

  public LeaveNode(boolean isSatisviable) {
    this.isSatisviable = isSatisviable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSatisfiable(int[] combination) {
    return isSatisviable;
  }

}

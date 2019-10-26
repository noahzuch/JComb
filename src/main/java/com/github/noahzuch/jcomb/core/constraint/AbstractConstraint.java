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
package com.github.noahzuch.jcomb.core.constraint;

/**
 * An Abstract class implementing the {@link Constraint} interface. It implements the
 * {@link Constraint#getInvolvedParameters()} method via an array that has to be supplied in the
 * constructor.
 * 
 * @author Noah
 *
 */
public abstract class AbstractConstraint implements Constraint {

  private int[] involvedParameters;

  /**
   * Creates a new AbstractConstraint.
   * 
   * @param involvedParameters The indices of the involved parameters of this constraint. Should be
   *        supplied by the extending class.
   */
  public AbstractConstraint(int[] involvedParameters) {
    this.involvedParameters = involvedParameters;
  }

  public int[] getInvolvedParameters() {
    return involvedParameters;
  }

}

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
package com.github.noahzuch.jcomb.junitjupiter;

import com.github.noahzuch.jcomb.core.InstanceDependent;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombContext;

/**
 * This class represents a combination of parameter values used as an input for one test method
 * call.
 * 
 * @author Noah Zuch
 *
 */
public class InputCombination implements InstanceDependent {

  private JCombContext context;
  private int[] combination;

  /**
   * Creates a new InputCombination for the given {@link JCombContext} and the supplied values
   * indices.
   * 
   * @param context The {@link JCombContext} for this input combination.
   * @param combination An int array that defines for each parameter the index of the values to use.
   */
  public InputCombination(JCombContext context, int[] combination) {
    this.context = context;
    this.combination = combination;
  }

  /**
   * Retrieves the value for a given parameter that is set for this combination.
   * 
   * @param paramIndex The index of the parameter.
   * @return The value of the parameter for this combination.
   */
  public Object getValueForParameter(int paramIndex) {
    return context.getParameter(paramIndex).getValueAt(combination[paramIndex]);
  }

  /**
   * Returns the index of the used value in this InputCombination for a given parameter index.
   * @param paramIndex The parameter index to get the used value from.
   * @return The used value index.
   */
  public int getValueIndexForParameter(int paramIndex) {
    return combination[paramIndex];
  }

  /**
   * Returns the number of parameters in this InputCombination.
   * @return The number of parameters in this InputCombination.
   */
  public int size() {
    return combination.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeForInstance(InstanceInformation instanceInformation) {
    context.initializeForInstance(instanceInformation);
  }

}

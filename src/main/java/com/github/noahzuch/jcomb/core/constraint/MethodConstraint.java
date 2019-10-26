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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import com.github.noahzuch.jcomb.core.JCombException;

/**
 * A MethodConstraint is an implementation of the {@link Constraint} interface that uses a method as
 * a constraint.
 * 
 * @author Noah
 *
 */
public class MethodConstraint extends AbstractConstraint {

  private Method method;

  /**
   * Creates a new MethodConstraint from the supplied method and involved parameters.
   * 
   * @param involvedParameters The parameters, that have to be given to the constraint method.
   * @param method the method that actually checks the constraint.
   */
  public MethodConstraint(int[] involvedParameters, Method method) {
    super(involvedParameters);
    if (!Modifier.isStatic(method.getModifiers())) {
      throw new JCombException("Constraint method " + method.getName()
          + " does not have the static modifier. Constraints have to be static.");
    }
    if (!(method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
      throw new JCombException(
          "A Constraint method has to return a boolean, but the Constraint method "
              + method.getName() + " does not.");
    }
    this.method = method;
    method.setAccessible(true);
  }

  @Override
  public boolean confirmsWith(Object[] inputKombination) {
    try {
      return (Boolean) method.invoke(null, inputKombination);
    } catch (IllegalAccessException e) {
      throw new JCombException("Can't access constraint method " + method.getName() + ".", e);
    } catch (IllegalArgumentException e) {
      throw new JCombException("The Methodparameters of the constraint '" + method.getName()
          + "' have different types than the defined Parameters in the constraints annotation:"
          + Arrays.toString(Arrays.stream(inputKombination).map(Object::getClass).toArray()), e);
    } catch (InvocationTargetException e) {
      throw new JCombException(
          "constraint method" + method.getName() + "threw an exception.", e);
    }
  }

}

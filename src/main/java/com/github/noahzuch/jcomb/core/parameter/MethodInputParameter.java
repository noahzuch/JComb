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
package com.github.noahzuch.jcomb.core.parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.github.noahzuch.jcomb.core.InstanceDependent;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.domain.Domain;

/**
 * A {@link NestedInputParameter}, that is defined via a Field.
 * 
 * @author Noah Zuch
 *
 */
public final class MethodInputParameter extends NestedInputParameter implements InstanceDependent {

  private Method method;

  /**
   * Creates a new {@link MethodInputParameter} for a given Method.
   * 
   * @param method The method object that represents the Parameter.
   */
  public MethodInputParameter(Method method) {
    this.method = method;
    method.setAccessible(true);
  }

  @Override
  public int getSize() {
    return super.getSize();
  }

  @Override
  public void initializeForInstance(InstanceInformation instanceInformation) {
    if (parameter == null || !isStaticMethod()) {
      parameter = getInputParameterForInstance(
          instanceInformation.getInstanceForClass(method.getDeclaringClass()));
    }
  }

  private boolean isStaticMethod() {
    return Modifier.isStatic(method.getModifiers());
  }

  private Domain getInputParameterForInstance(Object instance) {
    try {
      return parameter = (Domain) method.invoke(instance);
    } catch (IllegalArgumentException e) {
      throw new Error();
    } catch (IllegalAccessException e) {
      throw new JCombException("Can't access method " + method.getName() + ".");
    } catch (InvocationTargetException e) {
      throw new JCombException(
          "Method" + method.getName() + "threw an exception when getting the input parameters.");
    }
  }

}

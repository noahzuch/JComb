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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.github.noahzuch.jcomb.core.InstanceDependent;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.domain.Domain;

/**
 * A {@link NestedInputParameter} that is defined via a Field.
 * 
 * @author Noah Zuch
 *
 */
public final class FieldInputParameter extends NestedInputParameter implements InstanceDependent {

  private Field field;

  /**
   * Creates a new {@link FieldInputParameter} for a given Field.
   * 
   * @param field The field object that represents the parameter.
   */
  public FieldInputParameter(Field field) {
    this.field = field;
  }

  @Override
  public void initializeForInstance(InstanceInformation instanceInformation) {
    if (parameter == null || !isStaticField()) {
      parameter = getInputParameterForInstance(
          instanceInformation.getInstanceForClass(field.getDeclaringClass()));
    }
  }

  @Override
  public int getSize() {
    return super.getSize();
  }

  private boolean isStaticField() {
    return Modifier.isStatic(field.getModifiers());
  }

  private Domain getInputParameterForInstance(Object instance) {
    try {
      field.setAccessible(true);
      return parameter = (Domain) field.get(instance);
    } catch (IllegalArgumentException e) {
      throw new Error();
    } catch (IllegalAccessException e) {
      throw new JCombException("Can't access field " + field.getName() + ".");
    } finally {
      field.setAccessible(false);
    }
  }

}

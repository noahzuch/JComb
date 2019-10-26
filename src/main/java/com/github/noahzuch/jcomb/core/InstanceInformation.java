/**
 * Copyright 2019 Noah Zuch noahz97@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.noahzuch.jcomb.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import com.github.noahzuch.jcomb.annotations.InheritSetup;

/**
 * This class contains instantiated objects for all Classes from which Parameters or Constraints are
 * defined.
 * 
 * @author Noah
 *
 */
public class InstanceInformation {

  public Object testClassInstance;

  public Map<Class<?>, Object> objectMap;

  /**
   * Create a new {@link InstanceInformation} for the given test class instance.
   * 
   * @param testClassInstance The test class instance to create the {@link InstanceInformation}
   *        from.
   */
  public InstanceInformation(Object testClassInstance) {
    objectMap = new HashMap<>();
    setTestClassInstance(testClassInstance);
  }

  /**
   * Updates the {@link InstanceInformation} to a new test class instance.
   * 
   * @param testClassInstance The new test class instance for this {@link InstanceInformation}.
   */
  public void setTestClassInstance(Object testClassInstance) {
    this.testClassInstance = testClassInstance;
    objectMap.clear();
    objectMap.put(testClassInstance.getClass(), testClassInstance);
  }

  /**
   * Returns an instance of the given class.
   * 
   * @param clazz The class to get an instance from.
   * @return The instance or null, of no instance for the given class is present.
   */
  public Object getInstanceForClass(Class<?> clazz) {
    Object object = objectMap.get(clazz);
    if (object != null) {
      return object;
    }

    object = findInstance(testClassInstance, clazz);
    objectMap.put(clazz, object);
    return object;
  }

  private Object findInstance(Object currentObject, Class<?> clazz) {
    if (clazz.isInstance(currentObject)) {
      return currentObject;
    } else {
      for (Field f : ClassTools.getAllFieldsFromClass(currentObject.getClass())) {
        InheritSetup[] annotations = f.getAnnotationsByType(InheritSetup.class);
        if (annotations.length > 0) {
          f.setAccessible(true);
          Object inheritObject;
          try {
            inheritObject = f.get(currentObject);
          } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new JCombException("Could not access field " + f.getName() + " in class "
                + currentObject.getClass().getSimpleName() + ".", e);
          }
          Object object = findInstance(inheritObject, clazz);
          if (object != null) {
            return object;
          }
        }
      }
      // No Object found... should not happen
      throw new IllegalArgumentException(
          "This should not happen! There are no object instances in the current testsetup for Class"
              + " " + clazz.getSimpleName());

    }
  }

}

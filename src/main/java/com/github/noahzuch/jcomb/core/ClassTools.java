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
import java.util.Iterator;

/**
 * A tools class for accessing reflection information of class objects.
 * @author Noah
 *
 */
public class ClassTools {

 
  private ClassTools() {}

  /**
   * Returns all Fields of a Class and its super classes.
   * 
   * @param clazz The class to get all fields from.
   * @return An {@link Iterable} over all {@link Field} objects for the given class and its super
   *         classes.
   */
  public static Iterable<Field> getAllFieldsFromClass(Class<?> clazz) {
    return () -> {
      return new Iterator<Field>() {

        private Class<?> currentClass = clazz;
        private Field[] fields = clazz.getDeclaredFields();
        private int fieldIndex = 0;

        @Override
        public boolean hasNext() {
          if (fields.length > fieldIndex) {
            return true;
          } else {
            currentClass = currentClass.getSuperclass();
            if (currentClass == null || currentClass == Object.class) {
              return false;
            } else {
              fields = currentClass.getDeclaredFields();
              fieldIndex = 0;
              return true;
            }
          }
        }

        @Override
        public Field next() {
          return fields[fieldIndex++];
        }
      };
    };

  }
}

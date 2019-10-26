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

package com.github.noahzuch.jcomb.junitjupiter;

import java.util.Arrays;
import java.util.Collection;

public final class SmartToString {

  private SmartToString() {

  }

  /**
   * Returns a String representation of the given Object. If the object is an array or Collection,
   * an appropriate representation instead of the standard cryptic one is returned. For other
   * objects the standard {@link Object#toString()} method is used.
   * 
   * @param o The Object to get the string representation from.
   * @return The string representation of the object.
   */
  public static String toString(Object o) {
    if (o instanceof boolean[]) {
      return Arrays.toString((boolean[]) o);
    } else if (o instanceof int[]) {
      return Arrays.toString((int[]) o);
    } else if (o instanceof byte[]) {
      return Arrays.toString((byte[]) o);
    } else if (o instanceof short[]) {
      return Arrays.toString((short[]) o);
    } else if (o instanceof long[]) {
      return Arrays.toString((long[]) o);
    } else if (o instanceof double[]) {
      return Arrays.toString((double[]) o);
    } else if (o instanceof float[]) {
      return Arrays.toString((float[]) o);
    } else if (o instanceof char[]) {
      return Arrays.toString((char[]) o);
    } else if (o instanceof Object[]) {
      return Arrays.toString((Object[]) o);
    } else if (o instanceof Collection<?>) {
      return collectionToString((Collection<?>) o);
    } else {
      return String.valueOf(o);
    }
  }

  private static String collectionToString(Collection<?> c) {
    StringBuilder builder = new StringBuilder();
    c.forEach((item) -> builder.append(toString(item) + '\n'));
    return builder.toString();
  }

}

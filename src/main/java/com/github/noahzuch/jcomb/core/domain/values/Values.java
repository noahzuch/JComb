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
package com.github.noahzuch.jcomb.core.domain.values;

import java.util.Arrays;
import java.util.function.Supplier;
import com.github.noahzuch.jcomb.core.domain.Domain;

/**
 * This class is the default implementation of a {@link Domain}.
 * 
 * @author Noah Zuch
 *
 */
public class Values extends Domain {
  private Object[] values;

  /**
   * Creates a new Values object containing the supplied objects.
   * 
   * @param values The objects that should be contained in this Values object.
   * @param <T> The type of the values in this domain.
   */
  @SafeVarargs
  public <T> Values(T... values) {
    if (values.length == 0) {
      throw new IllegalArgumentException("Values can't be empty");
    }
    this.values = values;
  }

  /**
   * Creates a new Values object via the given supplier objects.
   * 
   * @param suppliers The suppliers that should be used to get the values from.
   * @param <T> The type of the values in this domain.
   */
  @SafeVarargs
  public <T> Values(Supplier<T>... suppliers) {
    values = new Object[suppliers.length];
    for (int i = 0; i < suppliers.length; i++) {
      values[i] = suppliers[i].get();
    }
  }

  /**
   * Creates a new Values object containing all values of the supplied enum.
   * 
   * @param enumClass The enum class from which the values should be saved.
   * @param <E> The type of the enum to create the domain from.
   */
  public <E extends Enum<E>> Values(Class<E> enumClass) {
    values = enumClass.getEnumConstants();
  }

  @Override
  public Object getValueAt(int index) {
    return values[index];
  }

  @Override
  public int getSize() {
    return values.length;
  }

  @Override
  public String toString() {
    return Arrays.toString(values);
  }
}

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
package com.github.noahzuch.jcomb.core.domain;

import com.github.noahzuch.jcomb.annotations.Parameter;

/**
 * The base class for Parameters that can be annotated via the {@link Parameter} annotation. 
 *
 */
public abstract class Domain {

  /**
   * Returns the value of the domain at the specified index.
   * @param index The index to get the value from.
   * @return The value at the given index.
   */
  public abstract Object getValueAt(int index);

  /**
   * Returns the number of values in this domain.
   * @return The number of values in this domain.
   */
  public abstract int getSize();

}

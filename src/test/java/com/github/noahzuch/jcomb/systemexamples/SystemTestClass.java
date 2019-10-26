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

package com.github.noahzuch.jcomb.systemexamples;

import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.domain.values.Values;

public class SystemTestClass {

  @Parameter(0)
  public Domain domain0 = new Values(1, 2, 3);

  @Parameter(1)
  public static Domain domain1 = new Values(4, 5, 6);

  @Parameter(2)
  public static Domain domain2 = new Values(7, 8, 9);

  @Parameter(3)
  public static Domain domain3 = new Values(10, 11, 12, 13);

  @Constraint(id = 0, parameters = {1, 0})
  public static boolean checkSameWithOffset(int a, int b) {
    return !(a == (b + 3));
  }

  @Constraint(id = 1, parameters = {2, 1, 0})
  public static boolean checkAllSameWithOffset(int a, int b, int c) {
    return !(a == (b + 3) && b == (c + 3));
  }

}

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.annotations.ValueIndex;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;

@ExtendWith(JCombExtension.class)
public class TestClass {

  @Parameter(0)
  public Values paramOne = new Values("1", "2", "3");

  @Parameter(1)
  public Ints paramTwo = new Ints(1, 2, 3);
  
  @Parameter(3)
  public Ints paramThree = new Ints(1, 2, 3);

  @JCombTest
  public void testMethod(String p1, int p2, @ValueIndex(1) int p2Index, int p3) {
    assertEquals(1, p2 - p2Index);
  }

}

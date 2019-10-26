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

package com.github.noahzuch.jcomb.core.generator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.domain.values.Values;

class SingleParameterInputGeneratorTest {

  @Test
  void testSuccess() {
    JCombContext context = mock(JCombContext.class);
    when(context.getParameterCount()).thenReturn(1);
    when(context.getParameter(0)).thenReturn(new Values(1, 2, 3, 4, 5, 6));
    SingleParameterInputGenerator generator = new SingleParameterInputGenerator(context);
    List<Object> result = generator.getAllInputCombinations().collect(Collectors.toList());
    for (int i = 0; i < result.size(); i++) {
      assertArrayEquals(new int[] {i}, (int[]) result.get(i));
    }
  }

  @Test
  void testMoreThan1Param() {
    JCombContext context = mock(JCombContext.class);
    when(context.getParameterCount()).thenReturn(2);
    when(context.getParameter(0)).thenReturn(new Values(1, 2, 3, 4, 5, 6));
    when(context.getParameter(1)).thenReturn(new Values(1, 2, 3, 4, 5, 6));
    try {
      SingleParameterInputGenerator generator = new SingleParameterInputGenerator(context);
      generator.toString();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("supplied context defines more than one parameter.", e.getMessage());
    }
  }

  @Test
  void testZeroParam() {
    JCombContext context = mock(JCombContext.class);
    when(context.getParameterCount()).thenReturn(0);
    SingleParameterInputGenerator generator;
    try {
      generator = new SingleParameterInputGenerator(context);
      generator.toString();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("no parameter supplied in given context.", e.getMessage());
    }
  }

}

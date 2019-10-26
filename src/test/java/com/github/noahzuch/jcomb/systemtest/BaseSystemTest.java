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
package com.github.noahzuch.jcomb.systemtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.core.JComb;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

public abstract class BaseSystemTest {

  protected JComb createjCombObject(Algorithm algorithm, int[] constraints, int[] parameters,
      int strength, Class<?> testClass) {
    JCombTest combTest = mock(JCombTest.class);
    when(combTest.algorithm()).thenReturn(algorithm);
    when(combTest.ignoreConstraints()).thenReturn(constraints.length == 0);
    when(combTest.constraints()).thenReturn(constraints);
    when(combTest.parameters()).thenReturn(parameters);
    when(combTest.strength()).thenReturn(strength);

    JComb jcomb = new JComb(testClass, combTest);
    return jcomb;
  }

}

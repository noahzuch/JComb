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
package com.github.noahzuch.jcomb.core.generator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.domain.Domain;

/**
 * A basic {@link TestGenerator} used if only 1 parameter is supplied.
 * 
 * @author Noah Zuch
 *
 */
public class SingleParameterInputGenerator implements TestGenerator {

  Domain parameter;

  /**
   * Creates a new generator for the given context. The parameter count has to be exactly one for
   * this generator to work.
   * 
   * @param context The context for the current test.
   */
  public SingleParameterInputGenerator(JCombContext context) {
    if (context.getParameterCount() > 1) {
      throw new IllegalArgumentException("supplied context defines more than one parameter.");
    }
    if (context.getParameterCount() == 0) {
      throw new IllegalArgumentException("no parameter supplied in given context.");
    }
    parameter = context.getParameter(0);
  }

  @Override
  public Stream<int[]> getAllInputCombinations() {
    return IntStream.rangeClosed(0, parameter.getSize() - 1).mapToObj(i -> new int[] {i});
  }

}

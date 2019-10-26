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

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import com.github.noahzuch.jcomb.core.JCombContext;

/**
 * The {@link TestTemplateInvocationContext} used by JComb.
 * 
 * @author Noah Zuch
 *
 */
public class JCombExtensionContext implements TestTemplateInvocationContext {

  private JCombContext jcombContext;
  private InputCombination inputCombination;

  /**
   * Creates a new {@link JCombExtensionContext} with the given parameters.
   * 
   * @param jcombContext The {@link JCombContext} of the current test. Has to be initialized for a
   *        test class instance.
   * @param inputCombination The {@link InputCombination} that should be used for this test
   *        execution.
   */
  public JCombExtensionContext(JCombContext jcombContext,
      InputCombination inputCombination) {

    this.jcombContext = jcombContext;
    this.inputCombination = inputCombination;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName(int invocationIndex) {
    StringBuilder builder = new StringBuilder();
    builder.append("[" + invocationIndex + "] : ("
        + SmartToString.toString(inputCombination.getValueForParameter(0)));
    for (int i = 1; i < inputCombination.size(); i++) {
      builder.append(", ");
      builder.append(SmartToString.toString(inputCombination.getValueForParameter(i)));
    }
    builder.append(")");
    return builder.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections
        .singletonList(new JCombParameterResolver(jcombContext, inputCombination));
  }
}

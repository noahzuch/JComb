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

import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import com.github.noahzuch.jcomb.annotations.ValueIndex;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombContext;

/**
 * A {@link ParameterResolver} implementation, that is used by JComb to resolve parameters via a
 * specific {@link InputCombination} generated previously.
 * 
 * @author Noah Zuch
 *
 */
public class JCombParameterResolver implements ParameterResolver {

  private InputCombination combination;
  private JCombContext context;

  private int currentParameter;

  /**
   * Creates a new {@link JCombParameterResolver} for the given parameters.
   * 
   * @param context The {@link JCombContext} of the current test instance.
   * @param combination The {@link InputCombination} used, to resolve parameters.
   */
  public JCombParameterResolver(JCombContext context, InputCombination combination) {
    this.combination = combination;
    this.context = context;
    currentParameter = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    if (currentParameter < context.getParameterCount()
        || parameterContext.isAnnotated(ValueIndex.class)) {
      InstanceInformation instanceInformation =
          new InstanceInformation(extensionContext.getRequiredTestInstance());
      combination.initializeForInstance(instanceInformation);
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    Optional<ValueIndex> valueIndexOpt = parameterContext.findAnnotation(ValueIndex.class);
    if (valueIndexOpt.isPresent()) {
      int paramIndex = valueIndexOpt.get().value();
      if (parameterContext.getParameter().getType() != int.class
          && parameterContext.getParameter().getType() != Integer.class) {
        throw new ParameterResolutionException(
            "The annotation ValueIndex can only be used with parameter types int or Integer");
      } else {
        return combination.getValueIndexForParameter(paramIndex);
      }
    } else {
      Object parameterInstance = combination.getValueForParameter(currentParameter);
      currentParameter++;
      return parameterInstance;
    }
  }

}

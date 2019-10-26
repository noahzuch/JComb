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

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.JComb;
import com.github.noahzuch.jcomb.core.JCombContext;

/**
 * The JUnit Extension needed, that a test class is using the JComb test instantiation via
 * combinatorial testing.
 * <p>
 * An Example of a testclass using JComb:
 * 
 * <pre>
 * &#64;ExtendWith(JCombExtension.class)
 * class JCombTest &#123;
 * 
 *     &#64;Parameter(0)
 *     Values parameter1 = new Values("Hello", "world", "!");
 * 
 *     &#64;Parameter(1)
 *     Values parameter2 = new Values(1, 2, 3);
 * 
 *     &#64;Parameter(2)
 *     Values getParameter3() &#123;
 *         return new Values(3.56d, 2.572d, 91234d);
 *     &#125;
 * 
 *     &#64;JCombTest
 *     void test(String a, int b, Double dd) &#123;
 *         ...
 *     &#125;
 * &#125;
 * </pre>
 * 
 * See the javadoc for {@link Parameter} and {@link JCombTest} for more details about each
 * annotation.
 * 
 * @author Noah
 * 
 */
public class JCombExtension implements TestTemplateInvocationContextProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return isAnnotated(context.getElement(), JCombTest.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
      final ExtensionContext context) {
    JCombTest annotation = findAnnotation(context.getElement(), JCombTest.class).get();
    Class<?> testClass = context.getRequiredTestClass();

    JComb jcomb = new JComb(testClass, annotation);
    JCombContext jcombContext = jcomb.getContext();

    return jcomb.createTestGenerator().getAllInputCombinations()
        .map(obj -> new JCombExtensionContext(
            jcombContext, new InputCombination(jcombContext, obj)));
  }

}

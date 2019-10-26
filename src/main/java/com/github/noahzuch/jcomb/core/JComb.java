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

package com.github.noahzuch.jcomb.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;
import com.github.noahzuch.jcomb.annotations.AnnotationFinder;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.core.generator.TestGenerator;
import com.github.noahzuch.jcomb.core.generator.TestGeneratorFactory;
import com.github.noahzuch.jcomb.core.partial.PartialJCombContext;

/**
 * The main Class to execute test generation for a given annotated testclass.
 * 
 * @author Noah
 *
 */
public class JComb {

  private JCombContext context;

  /**
   * Creates a new {@link JComb} object for the given test class and test information of the current
   * test method.
   * 
   * @param testClass The annotated test class.
   * @param testInformation The information about the test method.
   */
  public JComb(Class<?> testClass, JCombTest testInformation) {
    init(testClass, instantiateTestClassInstance(testClass), testInformation);
  }

  private void init(Class<?> testClass, Object testClassInstance, JCombTest testInformation) {
    context = AnnotationFinder.getContextFromAnnotatedClass(testClass);

    int[] parameterindizes = getParameterIndizes(testInformation, context);
    int[] relevantConstraints = getRelevantConstraints(testInformation, context);
    context = new PartialJCombContext(testInformation.algorithm(), testInformation.strength(),
        parameterindizes, relevantConstraints, context);
    InstanceInformation instanceInformation = new InstanceInformation(testClassInstance);
    context.initializeForInstance(instanceInformation);
  }

  /**
   * Creates a {@link TestGenerator} for this JComb instance.
   * 
   * @return A {@link TestGenerator}.
   */
  public TestGenerator createTestGenerator() {
    return TestGeneratorFactory.createGenerator(context);
  }

  /**
   * Returns the {@link JCombContext} used in this instance.
   * 
   * @return A {@link JCombContext}.
   */
  public JCombContext getContext() {
    return context;
  }

  private int[] getParameterIndizes(JCombTest annotation, JCombContext jcombContext) {
    int[] parameterIndizes = annotation.parameters();
    if (parameterIndizes == null || parameterIndizes.length == 0) {
      parameterIndizes = IntStream.range(0, jcombContext.getParameterCount()).toArray();
    }
    return parameterIndizes;
  }

  private int[] getRelevantConstraints(JCombTest annotation, JCombContext jcombContext) {
    int[] relevantConstraints = annotation.constraints();
    if (!annotation.ignoreConstraints()) {
      if (relevantConstraints == null || relevantConstraints.length == 0) {
        relevantConstraints = IntStream.range(0, jcombContext.getConstraints().size()).toArray();
      }
    } else {
      relevantConstraints = new int[0];
    }
    return relevantConstraints;
  }

  private Object instantiateTestClassInstance(Class<?> testClass) {
    // TODO Extend to non empty constructors. Use Junit initializsation somehow. Is in non public
    // core library
    try {
      Constructor<?> con = testClass.getDeclaredConstructor();
      con.setAccessible(true);
      return con.newInstance();
    } catch (IllegalAccessException | SecurityException | InstantiationException
        | IllegalArgumentException | NoSuchMethodException e) {
      throw new JCombException(
          "For JComb to work, the test class has to have an  accessable empty constructor");
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}

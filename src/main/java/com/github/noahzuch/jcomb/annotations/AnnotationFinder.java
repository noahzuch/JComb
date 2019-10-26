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
package com.github.noahzuch.jcomb.annotations;

import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.StandardJCombContext;
import com.github.noahzuch.jcomb.core.constraint.MethodConstraint;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.parameter.FieldInputParameter;
import com.github.noahzuch.jcomb.core.parameter.MethodInputParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * This class implements annotation scanning for jcomb.
 * 
 * @author Noah Zuch
 *
 */
public class AnnotationFinder {

  private Map<Integer, Domain> parameters;
  private StandardJCombContext context;
  private static HashMap<Class<?>, StandardJCombContext> contextMap = new HashMap<>();
  private List<Class<?>> scannedClasses;

  /**
   * Generates a {@link JCombContext} from the given Class.
   * 
   * @param classToScan the Class to generate the context from.
   * @return a {@link JCombContext} instance created from the given class.
   */
  public static JCombContext getContextFromAnnotatedClass(Class<?> classToScan) {
    StandardJCombContext context = contextMap.get(classToScan);
    if (context == null) {
      context = new StandardJCombContext();
      new AnnotationFinder(context).scan(classToScan);
      contextMap.put(classToScan, context);
    }
    return context;
  }

  /**
   * Creates a new instance for scanning in the supplied class.
   * 
   * @param classToScan The Class to scan annotations in.
   */
  private AnnotationFinder(StandardJCombContext context) {
    this.context = context;
    parameters = new HashMap<>();
    scannedClasses = new ArrayList<>();
  }

  /**
   * Scans the class for relevant annotations.
   */
  private void scan(Class<?> classToScan) {
    scanClass(classToScan);
    context.addParameters(parameters.entrySet().stream().sorted(Entry.comparingByKey())
        .map(Entry::getValue).collect(Collectors.toList()));

  }

  private void scanClass(Class<?> classToScan) {
    scannedClasses.add(classToScan);
    for (Field field : classToScan.getDeclaredFields()) {
      for (Annotation annotation : field.getAnnotations()) {
        scanField(field, annotation);
      }
    }
    for (Method method : classToScan.getDeclaredMethods()) {
      for (Annotation annotation : method.getAnnotations()) {
        scanMethod(context, method, annotation);
      }
    }
    Class<?> superClass = classToScan.getSuperclass();
    if (superClass != null && superClass != Object.class) {
      scanNewClass(superClass);
    }
  }

  private void scanMethod(StandardJCombContext context, Method method, Annotation annotation) {
    if (annotation instanceof Parameter) {
      if (!Domain.class.isAssignableFrom(method.getReturnType())) {
        throw new JCombException(
            "The Parameter Annotation can only be added to Methods that return a subclass of the Do"
            + "main interface");
      }
      addParameterMethodToParameters((Parameter) annotation, method);
    } else if (annotation instanceof Constraint) {
      addConstraintMethod(context, (Constraint) annotation, method);
    }
  }

  private void scanField(Field field, Annotation annotation) {
    if (annotation instanceof Parameter) {
      if (!Domain.class.isAssignableFrom(field.getType())) {
        throw new JCombException(
            "The Parameter Annotation can only be added to Fields that are subclasses of the Domain"
            + " interface");
      }
      addParameterFieldToParameters((Parameter) annotation, field);
    } else if (annotation instanceof InheritSetup) {
      scanNewClass(field.getType());
    }
  }

  private void scanNewClass(Class<?> clazz) {
    if (!scannedClasses.contains(clazz)) {
      scanClass(clazz);
    }
  }

  private void addConstraintMethod(StandardJCombContext context, Constraint constraint,
      Method method) {
    if (context.getConstraintsMap().containsKey(constraint.id())) {
      throw new JCombException("Multiple onstraint method use the same id " + constraint.id());
    } else {
      context.getConstraintsMap().put(constraint.id(),
          new MethodConstraint(constraint.parameters(), method));
    }
  }

  private void addParameterFieldToParameters(Parameter parameter,
      Field field) {
    if (parameters.containsKey(parameter.value())) {
      throw new JCombException(
          "The Parameter with id " + parameter.value() + " is defined multiple times.");
    } else {
      FieldInputParameter inputParameter = new FieldInputParameter(field);
      parameters.put(parameter.value(), inputParameter);
    }
  }

  private void addParameterMethodToParameters(Parameter parameter,
      Method method) {
    if (parameters.containsKey(parameter.value())) {
      throw new JCombException(
          "The Parameter with id " + parameter.value() + "is defined multiple times.");
    } else {
      MethodInputParameter inputParameter = new MethodInputParameter(method);
      parameters.put(parameter.value(), inputParameter);
    }
  }

}

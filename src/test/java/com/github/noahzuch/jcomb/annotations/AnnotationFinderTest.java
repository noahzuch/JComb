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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.systemexamples.InheritSetupClass;
import com.github.noahzuch.jcomb.systemexamples.ParameterClass;
import com.github.noahzuch.jcomb.systemexamples.SubClassTestClass;

class AnnotationFinderTest {

  @Test
  void testParameterFinding() {
    JCombContext context = AnnotationFinder.getContextFromAnnotatedClass(ParameterClass.class);

    ParameterClass instance = new ParameterClass();
    InstanceInformation instanceInfo = mock(InstanceInformation.class);
    when(instanceInfo.getInstanceForClass(ParameterClass.class)).thenReturn(instance);

    context.initializeForInstance(instanceInfo);
    assertParameterFindingNormal(context);
  }

  void assertParameterFindingNormal(JCombContext context) {
    assertDomainEquals(new Integer[] {1, 2, 3}, context.getParameter(0));
    assertDomainEquals(new Integer[] {4, 5, 6}, context.getParameter(1));
    assertDomainEquals(new Integer[] {7, 8, 9}, context.getParameter(2));
  }

  @Test
  void testConstraintFinding() {
    JCombContext context = AnnotationFinder.getContextFromAnnotatedClass(ParameterClass.class);

    ParameterClass instance = new ParameterClass();
    InstanceInformation instanceInfo = mock(InstanceInformation.class);
    when(instanceInfo.getInstanceForClass(ParameterClass.class)).thenReturn(instance);

    context.initializeForInstance(instanceInfo);

    Map<Integer, com.github.noahzuch.jcomb.core.constraint.Constraint> constraints =
        context.getConstraints();
    assertEquals(2, constraints.size());
    assertConstraintFindingNormal(constraints);
  }

  void assertConstraintFindingNormal(
      Map<Integer, com.github.noahzuch.jcomb.core.constraint.Constraint> constraints) {
    Constraint constraint0 = constraints.get(0);
    assertNotNull(constraint0);
    assertTrue(constraint0.confirmsWith(new Integer[] {5, 1}));
    assertFalse(constraint0.confirmsWith(new Integer[] {5, 2}));

    Constraint constraint1 = constraints.get(1);
    assertNotNull(constraint1);
    assertTrue(constraint1.confirmsWith(new Integer[] {7, 5, 2}));
    assertFalse(constraint1.confirmsWith(new Integer[] {8, 5, 2}));
  }

  @Test
  void testSuperClassInheritance() {
    JCombContext context = AnnotationFinder.getContextFromAnnotatedClass(SubClassTestClass.class);

    SubClassTestClass instance = new SubClassTestClass();
    InstanceInformation instanceInfo = mock(InstanceInformation.class);
    when(instanceInfo.getInstanceForClass(ParameterClass.class)).thenReturn(instance);
    when(instanceInfo.getInstanceForClass(SubClassTestClass.class)).thenReturn(instance);

    context.initializeForInstance(instanceInfo);

    assertParameterFindingNormal(context);
    assertDomainEquals(new Integer[] {10, 11, 12}, context.getParameter(3));

    Map<Integer, com.github.noahzuch.jcomb.core.constraint.Constraint> constraints =
        context.getConstraints();
    assertEquals(3, constraints.size());

    assertConstraintFindingNormal(constraints);

    Constraint constraint2 = constraints.get(2);
    assertNotNull(constraint2);
    assertTrue(constraint2.confirmsWith(new Integer[] {11, 1}));
    assertFalse(constraint2.confirmsWith(new Integer[] {11, 2}));
  }

  @Test
  void testInheritSetup() {
    JCombContext context = AnnotationFinder.getContextFromAnnotatedClass(InheritSetupClass.class);

    InheritSetupClass instance = new InheritSetupClass();
    InstanceInformation instanceInfo = mock(InstanceInformation.class);
    when(instanceInfo.getInstanceForClass(InheritSetupClass.class)).thenReturn(instance);
    when(instanceInfo.getInstanceForClass(ParameterClass.class)).thenReturn(instance.pClass);

    context.initializeForInstance(instanceInfo);
    assertParameterFindingNormal(context);
    assertDomainEquals(new Integer[] {10, 11, 12}, context.getParameter(3));

    Map<Integer, com.github.noahzuch.jcomb.core.constraint.Constraint> constraints =
        context.getConstraints();
    assertEquals(3, constraints.size());

    assertConstraintFindingNormal(constraints);

    Constraint constraint2 = constraints.get(2);
    assertNotNull(constraint2);
    assertTrue(constraint2.confirmsWith(new Integer[] {11, 1}));
    assertFalse(constraint2.confirmsWith(new Integer[] {11, 2}));
  }

  private void assertDomainEquals(Object[] values, Domain domain) {
    assertEquals(values.length, domain.getSize());
    for (int i = 0; i < values.length; i++) {
      assertEquals(values[0], domain.getValueAt(0));
    }
  }

}

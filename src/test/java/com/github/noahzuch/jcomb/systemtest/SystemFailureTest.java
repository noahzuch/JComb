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

package com.github.noahzuch.jcomb.systemtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.generator.Algorithm;

public class SystemFailureTest extends BaseSystemTest {

  private void assertJCombExceptionMessage(String message, JCombException e) {
    assertEquals("JComb encountered a problem with the current test class:\n" + message,
        e.getMessage());
  }

  @Test
  void testDoubleParam() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {}, new int[] {0}, 2, DoubleParamFailure.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage("The Parameter with id 0 is defined multiple times.", e);
    }
  }

  @Test
  void testDoubleConstraint() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {0}, new int[] {0, 1}, 2,
          DoubleConstrFailure.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage("Multiple onstraint method use the same id 0", e);
    }
  }

  @Test
  void testNonStaticConstraint() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {0}, new int[] {0, 1}, 2,
          NonStaticConstrFailure.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "Constraint method check does not have the static modifier. Constraints"
          + " have to be static.",
          e);
    }
  }

  @Test
  void testWrongReturnConstraint() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {0}, new int[] {0, 1}, 2,
          WrongReturnConstrFailure.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "A Constraint method has to return a boolean, but the Constraint method check does not.",
          e);
    }
  }

  @Test
  void testNonExistingParamConstraint() {
    // Parameter does not exist in testclass
    try {
      createjCombObject(Algorithm.ANY, new int[] {0}, new int[] {0, 1}, 2,
          NonExistingParamConstrFailure.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "A defined Constraint requires the parameter with id '3', but the"
          + " testmethod does not include this parameter.",
          e);
    }
    // parameter is excluded in testmethod
    try {
      createjCombObject(Algorithm.ANY, new int[] {0}, new int[] {0, 2}, 2, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "A defined Constraint requires the parameter with id '1', but the"
          + " testmethod does not include this parameter.",
          e);
    }
  }

  @Test
  void combTestNonExParamOrConstr() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {}, new int[] {0, 3}, 2, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "The testmethod requires a parameter with id '3', but no Parameter"
          + " with such an id is defined",
          e);
    }

    try {
      createjCombObject(Algorithm.ANY, new int[] {1}, new int[] {0, 1}, 2, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "The testmethod requires a constraint with id '1', but no Constraint"
          + " with such an id is defined",
          e);
    }
  }

  @Test
  void testCombTestDuplicateParameter() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {}, new int[] {0, 0}, 2, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "The parameter '0' is used twice for the testmethod. As of now defining"
          + " a parameter twice in a method is not allowed.",
          e);
    }
  }

  @Test
  void testStrengthToHighOrLow() {
    try {
      createjCombObject(Algorithm.ANY, new int[] {}, new int[] {0, 2}, 3, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage(
          "The strength of a combinatorial test can't be greated than the number of parameters", e);
    }

    try {
      createjCombObject(Algorithm.ANY, new int[] {}, new int[] {0, 2}, 0, ParamConstrBase.class);
      fail();
    } catch (JCombException e) {
      assertJCombExceptionMessage("The strength of a combinatorial test has to be at least 1", e);
    }
  }

  public static class DoubleParamFailure {

    @Parameter(0)
    private Domain domain1;

    @Parameter(0)
    private Domain domain2;

    public DoubleParamFailure() {}

  }

  private static class ParamBase {

    @Parameter(0)
    private Domain domain1 = new Ints(1, 2, 3);

    @Parameter(1)
    private Domain domain2 = new Ints(1, 2, 3);

    @Parameter(2)
    private Domain domain3 = new Ints(1, 2, 3);

    public ParamBase() {}

  }

  private static class ParamConstrBase extends ParamBase {

    @Constraint(id = 0, parameters = {0, 1})
    public static boolean check(int a, int b) {
      return a != b;
    }

    @SuppressWarnings("unused")
    public ParamConstrBase() {}

  }

  private static class DoubleConstrFailure extends ParamBase {

    @Constraint(id = 0, parameters = {0, 1})
    public static boolean check(int a, int b) {
      return true;
    }

    @Constraint(id = 0, parameters = {0, 1})
    public static boolean check2(int a, int b) {
      return true;
    }

    @SuppressWarnings("unused")
    public DoubleConstrFailure() {}
  }

  private static class NonStaticConstrFailure extends ParamBase {

    @Constraint(id = 0, parameters = {0, 1})
    public boolean check(int a, int b) {
      return true;
    }

    @SuppressWarnings("unused")
    public NonStaticConstrFailure() {}
  }

  private static class WrongReturnConstrFailure extends ParamBase {

    @Constraint(id = 0, parameters = {0, 1})
    public static int check(int a, int b) {
      return 0;
    }

    @SuppressWarnings("unused")
    public WrongReturnConstrFailure() {}
  }

  private static class NonExistingParamConstrFailure extends ParamBase {

    @Constraint(id = 0, parameters = {0, 3})
    public static boolean check(int a, int b) {
      return true;
    }

    @SuppressWarnings("unused")
    public NonExistingParamConstrFailure() {}
  }

}

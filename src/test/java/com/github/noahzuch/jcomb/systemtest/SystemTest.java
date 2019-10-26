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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.annotations.ValueIndex;
import com.github.noahzuch.jcomb.core.JComb;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.constraint.tree.ConstraintTree;
import com.github.noahzuch.jcomb.core.domain.values.Ints;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.core.generator.Algorithm;
import com.github.noahzuch.jcomb.core.generator.TestGenerator;
import com.github.noahzuch.jcomb.core.generator.ipog.IpogTestGenerator;
import com.github.noahzuch.jcomb.core.generator.ipog.IpogTestGeneratorTest;
import com.github.noahzuch.jcomb.core.generator.oa.OrthogonalArrayGenerator;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;
import com.github.noahzuch.jcomb.systemexamples.SystemTestClass;

@ExtendWith(JCombExtension.class)
public class SystemTest extends BaseSystemTest {

  @Parameter(0)
  static Values constraints =
      new Values(new int[] {}, new int[] {0});

  @Parameter(1)
  static Values parameters =
      new Values(new int[] {0, 1, 2}, new int[] {1, 2, 3}, new int[] {0, 3}, new int[] {1, 2});

  @Parameter(2)
  static Values algorithms = new Values(Algorithm.class);

  @Parameter(3)
  static Ints strengths = new Ints(2, 3);

  @Constraint(id = 0, parameters = {1, 3})
  private static boolean checkValidStrength(int[] parameters, int strength) {
    return strength <= parameters.length;
  }

  @Constraint(id = 1, parameters = {0, 1})
  private static boolean checkParamsExistForConstraint(int[] constraints, int[] parameters) {
    if (constraints.length > 0 && constraints[0] == 0) {
      return IntStream.of(parameters).filter(x -> (x == 0 || x == 1)).count() == 2;
    } else {
      return true;
    }
  }

  @Constraint(id = 2, parameters = {2, 1, 0, 3})
  public static boolean checkAlgorithmValid(Algorithm algorithm, int[] parameters,
      int[] constraints, int strength) {
    return !(algorithm == Algorithm.OA && (IntStream.of(parameters).anyMatch(x -> x == 3)
        || strength != 2 || constraints.length != 0));
  }

  @JCombTest(parameters = {2, 0, 1, 3}, strength = 3)
  void testValidCoveringArray(Algorithm algorithm, int[] constraints, int[] parameters,
      int strength, @ValueIndex(1) int parametersIndex) {
    JComb jcomb =
        createjCombObject(algorithm, constraints, parameters, strength, SystemTestClass.class);
    TestGenerator generator = jcomb.createTestGenerator();
    ConstraintHandler constraintHandler = new ConstraintTree(jcomb.getContext());

    int[] domainSizes = new int[parameters.length];
    for (int i = 0; i < domainSizes.length; i++) {
      if (parameters[i] == 3) {
        domainSizes[i] = 4;
      } else {
        domainSizes[i] = 3;
      }
    }

    List<int[]> array = generator.getAllInputCombinations().collect(Collectors.toList());
    IpogTestGeneratorTest.assertCoveringArray(domainSizes, array, strength, constraintHandler);

  }

  @JCombTest(parameters = {2, 0, 1, 3}, strength = 3, constraints = {0, 1})
  void testAlgorithmPicking(Algorithm algorithm, int[] constraints, int[] parameters,
      int strength) {
    JComb jcomb =
        createjCombObject(algorithm, constraints, parameters, strength, SystemTestClass.class);
    TestGenerator generator;

    if (!(algorithm == Algorithm.OA && (IntStream.of(parameters).anyMatch(x -> x == 3)
        || strength != 2 || constraints.length != 0))) {
      generator = jcomb.createTestGenerator();
    } else {
      try {
        generator = jcomb.createTestGenerator();
        fail();
      } catch (JCombException e) {
        return; // success
      }
    }

    if (algorithm == Algorithm.OA && !IntStream.of(parameters).anyMatch(x -> x == 3)
        && constraints.length == 0) {
      assertEquals(OrthogonalArrayGenerator.class, generator.getClass());
    } else if (algorithm == Algorithm.IPOG) {
      assertEquals(IpogTestGenerator.class, generator.getClass());
    } else if (IntStream.of(parameters).anyMatch(x -> x == 3) || !(constraints.length == 0)) {
      assertEquals(IpogTestGenerator.class, generator.getClass());
    } else {
      assertEquals(OrthogonalArrayGenerator.class, generator.getClass());
    }

  }

}

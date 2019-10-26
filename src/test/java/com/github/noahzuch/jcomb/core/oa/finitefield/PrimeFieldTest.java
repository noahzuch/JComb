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

package com.github.noahzuch.jcomb.core.oa.finitefield;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.noahzuch.jcomb.annotations.Constraint;
import com.github.noahzuch.jcomb.annotations.JCombTest;
import com.github.noahzuch.jcomb.annotations.Parameter;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.junitjupiter.JCombExtension;

@ExtendWith(JCombExtension.class)
public class PrimeFieldTest {

  @Parameter(0)
  private static Values primes = new Values(5, 7, 53);

  @Parameter(1)
  private static Values values1 = new Values(0, 1, 3, 6);

  @Parameter(2)
  private static Values values2 = new Values(0, 1, 2, 6);

  @Constraint(id = 0, parameters = {0, 1})
  public static boolean value1InRange(int p, int a) {
    return a < p;
  }

  @Constraint(id = 1, parameters = {0, 2})
  public static boolean value2InRange(int p, int a) {
    return a < p;
  }

  @JCombTest(parameters = {0, 1}, constraints = {0})
  void testMultInv(int p, int a) {
    PrimeField pf = new PrimeField(p);
    if (a == 0) {
      try {
        pf.multInvers(a);
        fail("Mult invers for zero should not exist");
      } catch (ArithmeticException e) {
        assertEquals("No multiplicative invers exists for zero", e.getMessage());
      }
    } else {
      assertEquals(1, pf.multiply(a, pf.multInvers(a)));
    }
  }

  @JCombTest(parameters = {0, 1}, constraints = {0})
  void testAddInv(int p, int a) {
    PrimeField pf = new PrimeField(p);
    assertEquals(0, pf.add(a, pf.addInvers(a)));
  }

  @JCombTest(parameters = {0, 1, 2})
  void testAddSubtr(int p, int a, int b) {
    PrimeField pf = new PrimeField(p);
    assertEquals(b, pf.add(a, pf.subtract(b, a)));
  }

  @JCombTest(parameters = {0, 1, 2})
  void testMultDiv(int p, int a, int b) {
    PrimeField pf = new PrimeField(p);
    if (a == 0) {
      try {
        pf.divide(b, a);
        fail("Div by zero should not be allowed");
      } catch (ArithmeticException e) {
        assertEquals("No multiplicative invers exists for zero", e.getMessage());
      }
    } else {
      assertEquals(b, pf.multiply(a, pf.divide(b, a)));
    }
  }

}

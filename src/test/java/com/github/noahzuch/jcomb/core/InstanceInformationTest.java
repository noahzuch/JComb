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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.systemexamples.InheritSetupClass;
import com.github.noahzuch.jcomb.systemexamples.ParameterClass;

class InstanceInformationTest {

  @Test
  void testInheritSetup() {
    InheritSetupClass object = new InheritSetupClass();
    InstanceInformation instanceInfo = new InstanceInformation(object);
    assertEquals(object, instanceInfo.getInstanceForClass(InheritSetupClass.class));
    assertEquals(object.pClass, instanceInfo.getInstanceForClass(ParameterClass.class));

  }

  @Test
  void testFailureForWrongClass() {
    InheritSetupClass object = new InheritSetupClass();
    InstanceInformation instanceInfo = new InstanceInformation(object);
    try {
      instanceInfo.getInstanceForClass(Integer.class);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(
          "This should not happen! There are no object instances in the"
              + " current testsetup for Class "
              + Integer.class.getSimpleName(),
          e.getMessage());
    }
  }

  @Test
  void testClassInheritance() {
    C object = new C();
    InstanceInformation instanceInfo = new InstanceInformation(object);
    assertEquals(object, instanceInfo.getInstanceForClass(A.class));
    assertEquals(object, instanceInfo.getInstanceForClass(B.class));
    assertEquals(object, instanceInfo.getInstanceForClass(C.class));

  }

}

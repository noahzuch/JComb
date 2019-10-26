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
package com.github.noahzuch.jcomb.core.parameter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.domain.values.Values;

class FieldInputParameterTest {

  private int[] value = new int[] {0, 1};

  public Values domain0 = new Values(value);

  private static int[] sValue = new int[] {0, 1};

  public static Values sDomain0 = new Values(sValue);

  @SuppressWarnings("null")
  @Test
  void testNonStaticField() {
    FieldInputParameter parameter = null;
    try {
      parameter = new FieldInputParameter(this.getClass().getDeclaredField("domain0"));
    } catch (NoSuchFieldException | SecurityException e) {
      assumeFalse(true, "Could not access field, that should exist");
    }
    FieldInputParameterTest instance = new FieldInputParameterTest();
    InstanceInformation instanceInfo = mockInstanceInformation(instance);

    parameter.initializeForInstance(instanceInfo);
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {0, 1}, (int[]) parameter.getValueAt(0));
    instance.value[0] = 1;
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {1, 1}, (int[]) parameter.getValueAt(0));
    instanceInfo = mockInstanceInformation(new FieldInputParameterTest());
    parameter.initializeForInstance(instanceInfo);
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {0, 1}, (int[]) parameter.getValueAt(0));
  }

  @SuppressWarnings({"null", "static-access"})
  @Test
  void testStaticField() {
    FieldInputParameter parameter = null;
    try {
      parameter = new FieldInputParameter(this.getClass().getDeclaredField("sDomain0"));
    } catch (NoSuchFieldException | SecurityException e) {
      assumeFalse(true, "Could not access field, that should exist");
    }
    FieldInputParameterTest instance = new FieldInputParameterTest();
    InstanceInformation instanceInfo = mockInstanceInformation(instance);

    parameter.initializeForInstance(instanceInfo);
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {0, 1}, (int[]) parameter.getValueAt(0));
    instance.sValue[0] = 1;
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {1, 1}, (int[]) parameter.getValueAt(0));
    instanceInfo = mockInstanceInformation(new FieldInputParameterTest());
    parameter.initializeForInstance(instanceInfo);
    assertEquals(1, parameter.getSize());
    assertArrayEquals(new int[] {1, 1}, (int[]) parameter.getValueAt(0));
  }

  private InstanceInformation mockInstanceInformation(FieldInputParameterTest instance) {
    InstanceInformation instanceInfo = mock(InstanceInformation.class);
    when(instanceInfo.getInstanceForClass(FieldInputParameterTest.class)).thenReturn(instance);
    return instanceInfo;
  }

}

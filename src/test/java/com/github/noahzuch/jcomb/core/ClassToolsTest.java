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

package com.github.noahzuch.jcomb.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.core.ClassTools;
import com.google.common.collect.Lists;

class ClassToolsTest {

  @Test
  void testTripleHierarchy() {
    Iterator<Field> fieldIterator = ClassTools.getAllFieldsFromClass(C.class).iterator();
    assertFields(1, 6, fieldIterator);
    assertFalse(fieldIterator.hasNext());
  }

  @Test
  void testDoubleHierarchy() {
    Iterator<Field> fieldIterator = ClassTools.getAllFieldsFromClass(B.class).iterator();
    assertFields(3, 6, fieldIterator);
    assertFalse(fieldIterator.hasNext());
  }

  @Test
  void testSingleHierarchy() {
    Iterator<Field> fieldIterator = ClassTools.getAllFieldsFromClass(A.class).iterator();
    assertFields(5, 6, fieldIterator);
    assertFalse(fieldIterator.hasNext());
  }

  void assertFields(int from, int to, Iterator<Field> fieldIt) {
    List<Field> fields = Lists.newArrayList(fieldIt);

    // Small hack to allow jacoco to add fields to the classes
    fields.removeIf(f -> f.getName().contains("jacoco"));

    assertEquals(to - from + 1, fields.size());
    for (int i = 0; i <= to - from; i++) {
      final String fieldName = "field" + (from + i);
      assertTrue(fields.stream().anyMatch(f -> f.getName().equals(fieldName)));
    }
  }

}

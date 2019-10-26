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
package com.github.noahzuch.jcomb.core.generator.ipog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

class OccurenceTrackerTest {

  @Test
  void testGetLeastFrequentValue() {
    OccurenceTracker tracker = new OccurenceTracker(new int[] {3});
    tracker.addUsedValue(0, 1);
    assertNotEquals(1, tracker.getLeastFrequentValue(0));

    tracker.addUsedValue(0, 2);
    tracker.addUsedValue(0, 2);
    tracker.addUsedValue(0, 0);
    tracker.addUsedValue(0, 0);

    assertEquals(1, tracker.getLeastFrequentValue(0));
  }

  @Test
  void testGetOccurenceIterator() {
    OccurenceTracker tracker = new OccurenceTracker(new int[] {3});
    tracker.addUsedValue(0, 0);
    tracker.addUsedValue(0, 0);
    tracker.addUsedValue(0, 1);

    Iterator<Integer> it = tracker.getOccurenceIterator(0);
    for (int i = 2; i >= 0; i--) {
      assertEquals(i, (int) it.next());
    }
    assertFalse(it.hasNext());
  }

}

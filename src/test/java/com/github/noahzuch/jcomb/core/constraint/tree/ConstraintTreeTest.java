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

package com.github.noahzuch.jcomb.core.constraint.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.constraint.AbstractConstraint;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.constraint.tree.ConstraintTree;
import com.github.noahzuch.jcomb.core.domain.values.Values;

class ConstraintTreeTest {

  private static Constraint constraint1 = new AbstractConstraint(new int[] {0, 1}) {

    @Override
    public boolean confirmsWith(Object[] inputKombination) {
      return (Integer) inputKombination[0] < (Integer) inputKombination[1];
    }
  };

  private static Constraint constraint2 = new AbstractConstraint(new int[] {0, 1, 2}) {

    @Override
    public boolean confirmsWith(Object[] inputKombination) {
      return (Integer) inputKombination[2] > (Integer) inputKombination[0]
          + (Integer) inputKombination[1];
    }
  };

  private static Constraint constraint3 = new AbstractConstraint(new int[] {1, 2}) {

    @Override
    public boolean confirmsWith(Object[] inputKombination) {
      return 10 > (Integer) inputKombination[0] + (Integer) inputKombination[1];
    }
  };

  @Test
  void testSingleConstraint() {
    List<Constraint> constraints = Arrays.asList(constraint1);

    ConstraintTree tree = new ConstraintTree(getMockedJCombContext(constraints));

    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, 9}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, 4}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, -1, 8}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, -1}));

    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, 9}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, 4}));
    assertEquals(false, tree.isSatisfiable(new int[] {10, -1, 8}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 0, -1}));

  }

  @Test
  void testTwoConstraint() {
    List<Constraint> constraints = Arrays.asList(constraint1, constraint2);

    ConstraintTree tree = new ConstraintTree(getMockedJCombContext(constraints));

    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, -1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, -1}));

    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {10, -1, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 0, -1}));

    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, 3}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, 8}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, -1, 2}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, 2}));

    assertEquals(false, tree.isSatisfiable(new int[] {0, 1, 1}));
    assertEquals(false, tree.isSatisfiable(new int[] {2, 5, 3}));
    assertEquals(false, tree.isSatisfiable(new int[] {4, 6, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {9, 10, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 10, -1}));

    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, 3}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 5, 8}));

    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, 1}));
    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, 2}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, 3}));
    assertEquals(false, tree.isSatisfiable(new int[] {4, 4, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {9, 3, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {10, -1, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 9, 9}));

  }

  @Test
  void testThreeConstraint() {
    List<Constraint> constraints = Arrays.asList(constraint1, constraint2, constraint3);

    ConstraintTree tree = new ConstraintTree(getMockedJCombContext(constraints));
    // constraint 1
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 3, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, -1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, -1}));

    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {10, -1, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 0, -1}));

    // contraint 2
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, 6}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 4, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, -1, 6}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, -1, 4}));

    assertEquals(false, tree.isSatisfiable(new int[] {-1, 5, 5}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 2, 8}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 10, 0}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 10, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, -1, 10}));

    // Constraint 3
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, 3}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 3, 6}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {2, 3, -1}));
    assertEquals(true, tree.isSatisfiable(new int[] {0, -1, 2}));
    assertEquals(true, tree.isSatisfiable(new int[] {-1, 1, 2}));

    assertEquals(false, tree.isSatisfiable(new int[] {0, 1, 1}));
    assertEquals(false, tree.isSatisfiable(new int[] {2, 5, 3}));
    assertEquals(false, tree.isSatisfiable(new int[] {4, 6, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {9, 10, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 10, -1}));

    // all constraints
    assertEquals(true, tree.isSatisfiable(new int[] {0, 1, 3}));
    assertEquals(true, tree.isSatisfiable(new int[] {1, 2, 7}));

    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, 1}));
    assertEquals(false, tree.isSatisfiable(new int[] {1, 0, 2}));
    assertEquals(false, tree.isSatisfiable(new int[] {5, 2, 3}));
    assertEquals(false, tree.isSatisfiable(new int[] {4, 4, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {9, 3, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {10, -1, -1}));
    assertEquals(false, tree.isSatisfiable(new int[] {-1, 9, 9}));
    assertEquals(false, tree.isSatisfiable(new int[] {3, 5, 9}));

  }

  private JCombContext getMockedJCombContext(List<Constraint> constraints) {
    Values param1 = new Values(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Values param2 = new Values(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Values param3 = new Values(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    JCombContext context = mock(JCombContext.class);
    when(context.getParameterCount()).thenReturn(3);
    when(context.getParameter(0)).thenReturn(param1);
    when(context.getParameter(1)).thenReturn(param2);
    when(context.getParameter(2)).thenReturn(param3);
    when(context.getConstraints()).thenReturn(IntStream.range(0, constraints.size()).boxed()
        .collect(Collectors.toMap(i -> i, constraints::get)));
    return context;
  }

}

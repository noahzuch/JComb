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

package com.github.noahzuch.jcomb.core.generator;

import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.JCombException;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.constraint.tree.ConstraintTree;
import com.github.noahzuch.jcomb.core.generator.ipog.IpogTestGenerator;
import com.github.noahzuch.jcomb.core.generator.oa.OrthogonalArrayGenerator;
import com.google.common.base.Preconditions;

/**
 * A factory class to create {@link TestGenerator}s based on the supplied information.
 * 
 * @author Noah Zuch
 *
 */
public class TestGeneratorFactory {

  private TestGeneratorFactory() {

  }

  /**
   * Creates a TestInputGenerator based on the supplied parameters.
   * 
   * @param context The JCombContext for the current test.
   * @return a TestInputGenerator applicable for the current test.
   */
  public static TestGenerator createGenerator(JCombContext context) {
    Preconditions.checkNotNull(context);
    if (context.getParameterCount() == 0) {
      throw new JCombException("No parameters supplied");
    }
    Algorithm algorithm = context.getAlgorithm();
    switch (algorithm) {
      case ANY:
        return createAnyGenerator(context);
      case OA:
        return createOrthogonalArrayGenerator(context);
      case IPOG:
        return createIpogGenerator(context);
      default:
        throw new IllegalArgumentException("Unrecognized algorithm type");
    }
  }

  private static TestGenerator createIpogGenerator(JCombContext context) {
    int[] domainSizes = createDomainSizesFromContext(context);
    ConstraintHandler constraintHandler = createConstraintHandlerFromContext(context);
    return new IpogTestGenerator(context.getStrength(), domainSizes, constraintHandler);
  }

  private static int[] createDomainSizesFromContext(JCombContext context) {
    int[] domainSizes = new int[context.getParameterCount()];
    for (int i = 0; i < domainSizes.length; i++) {
      domainSizes[i] = context.getParameter(i).getSize();
    }
    return domainSizes;
  }

  private static ConstraintHandler createConstraintHandlerFromContext(JCombContext context) {
    ConstraintHandler constraintHandler;
    if (context.getConstraints().size() > 0) {
      constraintHandler = new ConstraintTree(context);
    } else {
      constraintHandler = ConstraintHandler.getDefaultConstraintHandler();
    }
    return constraintHandler;
  }

  // private void sortForHighestDomainSize(int[] parameterSorting, int[] domainSizes) {
  // int n = domainSizes.length;
  // for (int i = 0; i < n - 1; i++) {
  // for (int j = 0; j < n - i - 1; j++) {
  // if (domainSizes[j] < domainSizes[j + 1]) {
  // swap(domainSizes,j,j+1);
  // swap(parameterSorting,j,j+1);
  // }
  // }
  // }
  // }
  //
  //
  //
  // private void swap(int[] array, int j, int k) {
  // int temp = array[j];
  // array[j] = array[k];
  // array[k] = temp;
  // }

  private static TestGenerator createOrthogonalArrayGenerator(JCombContext context) {
    int valueCount = context.getParameter(0).getSize();
    for (int k = 1; k < context.getParameterCount(); k++) {
      int tempValueCount = context.getParameter(k).getSize();
      if (tempValueCount != valueCount) {
        throw new JCombException(
            "Algorithm Orhtogonal Array needs all parameters to have the same number of possible va"
            + "lues.");
      }
      if (context.getStrength() > 2) {
        throw new JCombException(
            "Algorithm Orthogonal Array can not be used with a strength greater than 2.");
      }
      if (!context.getConstraints().isEmpty()) {
        throw new JCombException(
            "Algorithm Orthogonal Array can not be used with constraints. Use IPOG instead.");
      }
    }
    try {
      return new OrthogonalArrayGenerator(context.getParameterCount(), valueCount);
    } catch (IllegalArgumentException e) {
      throw new JCombException(e);
    }
  }

  private static TestGenerator createAnyGenerator(JCombContext context) {
    if (context.getParameterCount() == 1) {
      return new SingleParameterInputGenerator(context);
    } else if (context.getConstraints().size() > 0 || context.getStrength() > 2) {
      return createIpogGenerator(context);
    } else {
      try {
        return createOrthogonalArrayGenerator(context);
      } catch (JCombException e) {
        // fallback
        return createIpogGenerator(context);
      }
    }
  }

}

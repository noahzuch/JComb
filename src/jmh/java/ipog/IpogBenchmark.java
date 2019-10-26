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
package ipog;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.*;
import com.github.noahzuch.jcomb.core.InstanceInformation;
import com.github.noahzuch.jcomb.core.JCombContext;
import com.github.noahzuch.jcomb.core.constraint.Constraint;
import com.github.noahzuch.jcomb.core.constraint.ConstraintHandler;
import com.github.noahzuch.jcomb.core.constraint.MethodConstraint;
import com.github.noahzuch.jcomb.core.constraint.tree.ConstraintTree;
import com.github.noahzuch.jcomb.core.domain.Domain;
import com.github.noahzuch.jcomb.core.domain.values.Values;
import com.github.noahzuch.jcomb.core.generator.Algorithm;
import com.github.noahzuch.jcomb.core.generator.ipog.IpogTestGenerator;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
public class IpogBenchmark {

  // @Param({"2", "3", "4", "5", "6", "7"})
  public int strength = 2;

  @Param({"7"})
  public int values;

  @Param({"7"})
  public int parameters;

  @Param({"3"})
  public int useConstraint;
  // @Param();
  // public static final int[][] parameterDomain =
  // {{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},{7,7,7,7,7,7,7,7,7,7,7,7,7,7,7},{7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7},{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10},{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10}};

  private Domain[] domains;

  int[] domainSizes;

  private Map<Integer, Constraint> constraintMap;

  private Constraint constraint1 = new Constraint() {

    private int[] invConstr = new int[] {0, 3};

    @Override
    public int[] getInvolvedParameters() {
      return invConstr;
    }

    @Override
    public boolean confirmsWith(Object[] inputCombination) {
      return ((Integer) inputCombination[0]) < ((Integer) inputCombination[1]);
    }
  };

  private Constraint constraint2 = new Constraint() {

    private int[] invConstr = new int[] {2, 3};

    @Override
    public int[] getInvolvedParameters() {
      return invConstr;
    }

    @Override
    public boolean confirmsWith(Object[] inputCombination) {
      return ((Integer) inputCombination[0]) != ((Integer) inputCombination[1]);
    }
  };

  private Constraint constraint3 = new Constraint() {

    private int[] invConstr = new int[] {2, 3};

    @Override
    public int[] getInvolvedParameters() {
      return invConstr;
    }

    @Override
    public boolean confirmsWith(Object[] inputCombination) {
      return ((Integer) inputCombination[0]) < 2 * ((Integer) inputCombination[1]);
    }
  };

  public static JCombContext context;
  private IpogTestGenerator generator;

  public static boolean isGreater(int a, int b) {
    return b > a;
  }

  @Benchmark
  public void ipogBenchmark(IpogBenchmark state, Blackhole bh) {
    state.generator = new IpogTestGenerator(strength, domainSizes, new ConstraintTree(context));
    bh.consume(state.generator.getAllInputCombinations());
  }

  @Setup
  public void setup() {
    domainSizes = new int[parameters];
    Arrays.fill(domainSizes, values);
    domains = new Domain[parameters];
    for (int i = 0; i < domains.length; i++) {
      Integer[] array = new Integer[values];
      for (int j = 0; j < array.length; j++) {
        array[j] = j;
      }
      domains[i] = new Values(array);
    }

    if (useConstraint == 1) {
      constraintMap = Collections.singletonMap(0, constraint1);
    } else if (useConstraint == 2) {
      constraintMap = new HashMap<Integer, Constraint>();
      constraintMap.put(0, constraint1);
      constraintMap.put(1, constraint2);
    } else if (useConstraint == 3) {
      constraintMap = new HashMap<Integer, Constraint>();
      constraintMap.put(0, constraint1);
      constraintMap.put(1, constraint2);
      constraintMap.put(2, constraint3);

    } else {
      constraintMap = Collections.emptyMap();
    }

    context = new JCombContext() {

      @Override
      public Map<Integer, Constraint> getConstraints() {
        return constraintMap;
      }

      @Override
      public Domain getParameter(int index) {
        return domains[index];
      }

      @Override
      public int getParameterCount() {
        return domainSizes.length;
      }

      @Override
      public int getStrength() {
        return 2;
      }

      @Override
      public Algorithm getAlgorithm() {
        return Algorithm.ANY;
      }

      @Override
      public void initializeForInstance(InstanceInformation instanceInformation) {
        // TODO Auto-generated method stub

      }
    };
    // generator = new IpogTestGenerator(strength,domainSizes, test -> true);

  }

  // public static void main(String[] args) throws RunnerException {
  // Options opt = new OptionsBuilder()
  // .include(".*" + IpogBenchmark.class.getSimpleName() + ".*")
  // .warmupIterations(5)
  // .measurementIterations(5)
  // .threads(4)
  // .forks(1)
  // .build();
  //
  // new Runner(opt).run();
  // }

}

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
package oa;

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
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import com.github.noahzuch.jcomb.core.generator.oa.OrthogonalArrayGenerator;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
public class OA4ParamBenchmark {

  private OrthogonalArrayGenerator generator;

  @Param({"3", "5", "12", "15", "31", "49", "75", "97", "155", "201"})
  public int size;

  @Benchmark
  public void oaBenchmark4Param(OA4ParamBenchmark state, Blackhole bh) {
    bh.consume(state.generator.getAllInputCombinations());
  }

  @Setup
  public void setup() {
    generator = new OrthogonalArrayGenerator(4, size);
  }

  // public static void main(String[] args) throws RunnerException {
  // Options opt = new OptionsBuilder()
  // .include(".*" + OA4ParamBenchmark.class.getSimpleName() + ".*")
  // .warmupIterations(5)
  // .measurementIterations(5)
  // .threads(4)
  // .forks(1)
  // .build();
  //
  // new Runner(opt).run();
  // }

}

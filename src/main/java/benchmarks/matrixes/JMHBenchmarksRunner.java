/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergey Pomelov.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package benchmarks.matrixes;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;

import util.GNUCopyright;

import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Mode.Throughput;

/**
 * JMH benchmarks run class.
 * This class can't be final or not public because runtime instrumentation reasons.
 * @author Sergey Pomelov on 06/04/16.
 */
@Fork(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
@BenchmarkMode(AverageTime)
@Timeout(time = 30, timeUnit = TimeUnit.SECONDS)
@ParametersAreNonnullByDefault
public class JMHBenchmarksRunner {

    @Param({"1", "2", "4", "8"})
    private int threads;
    //@Param({"10", "50", "100", "1000", "2000", "5000"})
    private int size;

    public static void main(String... args) throws RunnerException {
        GNUCopyright.printLicence();
        final Options options = new OptionsBuilder()
                .include(JMHBenchmarksRunner.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public void matrixPow() {
        MatrixTasks.matrixPow(threads);
    }

    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 3, time = 3)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @BenchmarkMode(Throughput)
    @Threads(-1)
    public void memoryAlloc(Blackhole blackhole) {
        blackhole.consume(MatrixTasks.memoryAlloc(size));
    }
}

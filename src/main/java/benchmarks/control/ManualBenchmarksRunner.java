/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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

package benchmarks.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.control.metrics.Measurer;
import benchmarks.control.metrics.PerformanceRecord;
import benchmarks.tasks.martixes.MatrixTasks;

import static util.ConversionUtil.bytesToMb;
import static util.TimeUtil.nanoToMls;

/**
 * @author Sergey Pomelov on 06.04.16.
 */
@SuppressWarnings("SameParameterValue")
@ParametersAreNonnullByDefault
final class ManualBenchmarksRunner {

    private static final Logger log = LoggerFactory.getLogger(ManualBenchmarksRunner.class);

    private static final int ITERATIONS = 10;
    private static final int MAX_DEGREE = 4;

    private ManualBenchmarksRunner() { /* runnable class */ }

    public static void main(String... args) {
        printStats();
        waitForInput();
        log.info("started");
        runBenchmark();
        log.info("press to quit");
        waitForInput();
        System.exit(0);
    }

    private static void printStats() {
        GNUCopyright.printLicence();
        log.info("Press to start. Cores: {}. Memory: {} Mb.",
                Runtime.getRuntime().availableProcessors(),
                bytesToMb(Runtime.getRuntime().maxMemory()));
    }

    private static void runBenchmark() {
        ManagementFactory.getThreadMXBean().setThreadCpuTimeEnabled(true);
        for (int i = 0; i <= MAX_DEGREE; i++) {
            final int twoPowI = Math.toIntExact(Math.round(StrictMath.pow(2, i)));
            measure(() -> MatrixTasks.matrixPow(twoPowI), ITERATIONS, twoPowI + " threads");
        }
    }

    private static void measure(Runnable task, int iterations, String label) {
        warmUp(task, iterations, label);
        rampUp(task, iterations, label);
    }

    private static void waitForInput() {
        int input = 0;
        try {
            input = System.in.read();
        } catch (IOException e) {
            log.error("{} {}", input, e.getStackTrace());
        }
    }

    private static void warmUp(Runnable task, int iterations, String label) {
        log.debug("warm up start {}", label);
        runSeveralTimes(task, iterations);
        log.info("warm up end {}", label);
    }

    private static void runSeveralTimes(Runnable task, int iterations) {
        for (int i = 0; i < iterations; i++) {
            task.run();
        }
    }

    private static void rampUp(Runnable task, int iterations, String label) {
        log.debug("measuring start {}", label);
        final PerformanceRecord info = new Measurer().measurePerformance(() ->
                runSeveralTimes(task, iterations), label);
        log.info("{}. Time avg: {}ms. Cpu total: {}ms. User total: {}ms.",
                label,
                (nanoToMls(info.getTime()) / iterations),
                (nanoToMls(info.getCpuTime())),
                (nanoToMls(info.getUserTime())));
    }
}

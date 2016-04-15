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

package benchmarks.tasks.martixes;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import benchmarks.control.metrics.Measurer;

/**
 * @author Sergey Pomelov on 06.04.16.
 */
@SuppressWarnings("SameParameterValue")
final class TasksRunner {

    private TasksRunner() { /* utility class */ }

    static void runExperiment(int threads, int runs, int problemSize, int taskMultiplicator) {
        final Runnable divider = generateTask(
                DataProcessor.generateRandomArray(problemSize, problemSize / threads),
                taskMultiplicator);
        Reporter.report(new Measurer().measurePerformance(() -> {
            for (int i = 0; i < runs; i++) {
                final ThreadPoolExecutor executor = generateExecutor(threads);
                for (int threadIdx = 1; threadIdx <= threads; threadIdx++) {
                    executor.submit(divider);
                }
                awaitShutdown(executor);
            }
        }, "task"));
    }

    private static Runnable generateTask(byte[][] data, int taskMultiplicator) {
        return () -> Reporter.report(new Measurer().measurePerformance(() ->
                DataProcessor.processArray(data, taskMultiplicator), "parallel task"));
    }

    private static ThreadPoolExecutor generateExecutor(int size) {
        return new ThreadPoolExecutor(size, size, 1,
                TimeUnit.NANOSECONDS, new ArrayBlockingQueue<>(1), new ThreadFactoryImpl());
    }

    @SuppressWarnings("MethodCallInLoopCondition")
    private static void await(ThreadPoolExecutor executor) {
        while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) {
            }
        }
    }

    private static void awaitShutdown(ThreadPoolExecutor executor) {
        await(executor);
        executor.shutdownNow();
    }
}

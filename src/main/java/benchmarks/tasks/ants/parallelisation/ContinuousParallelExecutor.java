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

package benchmarks.tasks.ants.parallelisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Sergey Pomelov on 04/05/2016.
 */
@ParametersAreNonnullByDefault
public final class ContinuousParallelExecutor {

    private static final Logger log = LoggerFactory.getLogger(ContinuousParallelExecutor.class);
    private static final String INTERRUPTED_EX = "Got an interrupted exception!";

    private ContinuousParallelExecutor() { /* utility class*/ }

    public static void run(Callable<Long> agent, @Nonnegative int parallelAgents,
                           Supplier<Boolean> terminationCondition,
                           Runnable agentsRunAdditionalOperation,
                           String poolName, String agentName) {
        final ExecutorService executor = buildContinuousExecutor(parallelAgents,
                poolName, agentName);
        for (int i = 0; i < parallelAgents; i++) {
            executor.submit(agent);
        }

        try {
            mainCycle(agent, parallelAgents, terminationCondition,
                    agentsRunAdditionalOperation, executor);
            terminate(executor);
        } catch (InterruptedException e) {
            log.warn(INTERRUPTED_EX, e);
        } finally {
            executor.shutdownNow();
        }
    }

    private static void mainCycle(Callable<Long> agent,
                                  int parallelAgents,
                                  Supplier<Boolean> terminationCondition,
                                  Runnable agentsRunAdditionalOperation,
                                  ExecutorService executor) throws InterruptedException {
        final List<Future<Long>> runFutureResults = executor.
                invokeAll(Collections.nCopies(parallelAgents, agent));
        while (!terminationCondition.get()) {
            //log.debug("s {}/{}", runFutureResults.size(), finished.size());
            addTasksIfNeed(runFutureResults, executor, agent, parallelAgents);
            agentsRunAdditionalOperation.run();
            Thread.sleep(100);
            //log.debug("e {}/{}", runFutureResults.size(), finished.size());
        }
    }

    private static void addTasksIfNeed(List<Future<Long>> runFutureResults,
                                       ExecutorService executor, Callable<Long> agent, int parallelAgents) {
        final List<Future<Long>> finished = getFinished(runFutureResults);
        runFutureResults.removeAll(finished);
        if (runFutureResults.size() < (parallelAgents * 20)) {
            for (int i = 0; i < (parallelAgents * 20); i++) {
                runFutureResults.add(executor.submit(agent));
            }
        }
    }

    private static void terminate(ExecutorService executor) throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(10L, TimeUnit.NANOSECONDS);
    }

    private static List<Future<Long>> getFinished(List<Future<Long>> runResults) {
        return runResults.stream().filter(Future::isDone).collect(Collectors.toList());
    }

    private static ExecutorService buildContinuousExecutor(int parallelAgents,
                                                           String poolName, String agentName) {
        return Executors.newFixedThreadPool(parallelAgents,
                new AgentsThreadsFactory(poolName, agentName));
    }
}

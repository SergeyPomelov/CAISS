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

package benchmarks.ants.colonies.parallelisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Sergey Pomelov on 04/05/2016.
 * Creates a pool of constant amount of threads and keeps ot beasy with agents tasks.
 */
@SuppressWarnings("ClassUnconnectedToPackage") // false claim
@ParametersAreNonnullByDefault
public final class ContinuousParallelExecutor {

    private static final Logger log = LoggerFactory.getLogger(ContinuousParallelExecutor.class);
    private static final String INTERRUPTED_EX = "Got an interrupted exception!";
    private static final int BASE_MULTIPLIER = 1000;
    private static final int TASKS_QUEUE_CHECK_INTERVAL_MLS = 300;

    private ContinuousParallelExecutor() { /* utility class*/ }

    @SuppressWarnings("SameParameterValue")
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
        int tasksReserve = BASE_MULTIPLIER;
        boolean isFirstCycle = true;
        //noinspection MethodCallInLoopCondition, by design
        while (!terminationCondition.get()) {
            tasksReserve = addTasksIfNeed(runFutureResults, executor, agent,
                    isFirstCycle, tasksReserve);
            agentsRunAdditionalOperation.run();
            isFirstCycle = false;
            Thread.sleep(TASKS_QUEUE_CHECK_INTERVAL_MLS);
        }
    }

    private static int addTasksIfNeed(Collection<Future<Long>> runFutureResults,
                                      ExecutorService executor, Callable<Long> agent,
                                      boolean firstCycle, int tasksReserve) {
        int revisedTasksReserve = tasksReserve;
        final List<Future<Long>> finished = getFinished(runFutureResults);
        runFutureResults.removeAll(finished);
        if (runFutureResults.size() < tasksReserve) {
            revisedTasksReserve = reviseTasksReserve(firstCycle, tasksReserve, runFutureResults);
            addTasks(executor, agent, runFutureResults, tasksReserve);
        }
        return revisedTasksReserve;
    }

    private static int reviseTasksReserve(boolean firstCycle, int tasksReserve,
                                          Collection<Future<Long>> runFutureResults) {
        if (!firstCycle && runFutureResults.isEmpty()) {
            final int revisedTasksReserve = tasksReserve * 2;
            log.debug("Zero tasks, new queue size {} -> {}.", tasksReserve, revisedTasksReserve);
            return revisedTasksReserve;
        }
        return tasksReserve;
    }

    private static void addTasks(ExecutorService executor, Callable<Long> agent,
                                 Collection<Future<Long>> runFutureResults,
                                 int tasksReserve) {
        for (int i = 0; i < tasksReserve; i++) {
            runFutureResults.add(executor.submit(agent));
        }
    }

    private static List<Future<Long>> getFinished(Collection<Future<Long>> runResults) {
        return runResults.stream().filter(Future::isDone).collect(Collectors.toList());
    }

    private static ExecutorService buildContinuousExecutor(int parallelAgents,
                                                           String poolName, String agentName) {
        return Executors.newFixedThreadPool(parallelAgents,
                new AgentsThreadsFactory(poolName, agentName));
    }
}

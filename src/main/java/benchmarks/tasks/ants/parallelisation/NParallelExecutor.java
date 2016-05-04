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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.tasks.ants.AntsColony;

/**
 * Class for executing n parallel ants.
 * @author Sergey Pomelov on 29/04/2016.
 * @see AntsColony
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
public final class NParallelExecutor {

    private NParallelExecutor() { /* utility class */ }

    public static void runExecutor(Runnable agent, @Nonnegative int parallelAgents,
                                   String poolName, String agentName) {
        runExecutor(Collections.nCopies(parallelAgents, agent), poolName, agentName);
    }

    public static void runExecutor(List<Runnable> agents, String poolName, String agentName) {
        final ThreadPoolExecutor executor = buildExecutor(agents.size(), poolName, agentName);
        agents.forEach(executor::execute);
        awaitExecution(executor, agents.size());
    }

    private static void awaitExecution(ThreadPoolExecutor executor, int parallelAgents) {
        //noinspection MethodCallInLoopCondition - ok, the condition must be checked every time.
        while (executor.getCompletedTaskCount() < parallelAgents) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
                // we are waiting
            }
        }
    }

    private static ThreadPoolExecutor buildExecutor(int size, String poolName, String agentName) {
        return AgentsThreadPoolExecutorBuilder.build(size, poolName, agentName);
    }
}

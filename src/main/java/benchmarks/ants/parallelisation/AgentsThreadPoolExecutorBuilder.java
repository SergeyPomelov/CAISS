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

package benchmarks.ants.parallelisation;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Sergey Pomelov on 28/04/2016.
 * Adds to the standart implementation pool and agents names to the threads names for buisness
 * model view-point readability.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
final class AgentsThreadPoolExecutorBuilder {

    private AgentsThreadPoolExecutorBuilder() { /* package-local utility class */ }

    static ThreadPoolExecutor build(@Nonnegative int parallelTasks,
                                    String poolName, String agentName) {
        return new ThreadPoolExecutor(parallelTasks, parallelTasks, 1, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new AgentsThreadsFactory(poolName, agentName));
    }
}

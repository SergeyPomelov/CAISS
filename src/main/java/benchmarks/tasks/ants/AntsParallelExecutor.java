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

package benchmarks.tasks.ants;

import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnegative;

/**
 * @author Sergey Pomelov on 29/04/2016. Class for executing n parallel ants.
 * @see AntsColony
 */
final class AntsParallelExecutor {

    private AntsParallelExecutor() { /* package-local utility class */ }

    static void runExecutor(Runnable antRun, @Nonnegative int parallelAnts) {
        final ThreadPoolExecutor executor = AntsThreadPoolExecutorBuilder.build();
        for (int i = 0; i < parallelAnts; i++) {
            executor.execute(antRun);
        }
        //noinspection MethodCallInLoopCondition - completed tasks need be checked every time
        while (executor.getCompletedTaskCount() < parallelAnts) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
                // we are waiting
            }
        }
        executor.shutdown();
    }
}

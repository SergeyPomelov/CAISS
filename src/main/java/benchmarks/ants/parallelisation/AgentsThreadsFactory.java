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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Sergey Pomelov on 28/04/2016.
 */
@ParametersAreNonnullByDefault
final class AgentsThreadsFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    AgentsThreadsFactory(String poolName, String agentName) {
        final SecurityManager securityManager = System.getSecurityManager();
        group = (securityManager != null) ?
                securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = poolName + '-' + poolNumber.getAndIncrement() + '-' + agentName + '-';
    }

    @Override
    public Thread newThread(Runnable run) {
        final Thread thread = new Thread(group, run,
                namePrefix + threadNumber.getAndIncrement(), 0);
        if (!thread.isDaemon()) {
            thread.setDaemon(true);
        }
        return thread;
    }
}

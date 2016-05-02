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

package benchmarks.control.metrics;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Class for measuring nano, cpu and user time elapsed while the passed task runs.
 * @author Sergey Pomelov on 06/04/16.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@ThreadSafe
@ParametersAreNonnullByDefault
public final class Measurer {

    @Nonnull
    private final Map<String, List<PerformanceRecord>> measures = new ConcurrentHashMap<>(4, 0.75f);

    public PerformanceRecord measurePerformance(Runnable task, String label) {
        final long startNanoTime = System.nanoTime();
        final long startCpuTime = getCpuTimeNano();
        final long startUserTime = getUserTimeNano();
        task.run();
        final PerformanceRecord performanceRecord =
                new PerformanceRecord(label,
                        System.nanoTime() - startNanoTime,
                        getCpuTimeNano() - startCpuTime,
                        getUserTimeNano() - startUserTime);
        measures.getOrDefault(label, new CopyOnWriteArrayList<>()).add(performanceRecord);
        return performanceRecord;
    }

    @Nonnull
    public Map<String, List<PerformanceRecord>> getMeasures() {
        return measures;
    }

    private static long getCpuTimeNano() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    private static long getUserTimeNano() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadUserTime();
    }
}

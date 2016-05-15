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

package benchmarks.matrixes.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Class for measuring nano, cpu and user time elapsed while the passed task runs.
 *
 * @author Sergey Pomelov on 06/04/16.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class PerformanceMeasurer implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMeasurer.class);
    private static final long serialVersionUID = -6814226576965075844L;

    @Nonnull
    private final Map<String, List<PerformanceRecord>> measures = new HashMap<>(1, 0.5f);
    @Nonnull
    private final Map<String, Long> calls = new HashMap<>(1, 0.5f);
    private final boolean recordMXBeanTimes;

    public PerformanceMeasurer() {
        this(false);
    }

    public PerformanceMeasurer(boolean recordMXBeanTimes) {
        this.recordMXBeanTimes = recordMXBeanTimes;
    }

    public static PerformanceMeasurer compileOverall(List<PerformanceMeasurer>
                                                             performanceMeasurers) {
        final PerformanceMeasurer overallMeasurer = new PerformanceMeasurer();
        filOverallMetrics(overallMeasurer, performanceMeasurers);
        return overallMeasurer;
    }

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
        registerMeasurement(label, performanceRecord);
        return performanceRecord;
    }

    @Nullable
    public <T> T measurePerformanceCallable(Callable<T> task, String label) {
        final long startNanoTime = System.nanoTime();
        final long startCpuTime = getCpuTimeNano();
        final long startUserTime = getUserTimeNano();
        T result = null;
        try {
            result = task.call();
        } catch (Exception e) {
            log.error("Callable error report while measurements!", e);
        }
        final PerformanceRecord performanceRecord =
                new PerformanceRecord(label,
                        System.nanoTime() - startNanoTime,
                        getCpuTimeNano() - startCpuTime,
                        getUserTimeNano() - startUserTime);
        registerMeasurement(label, performanceRecord);
        return result;
    }

    public long getCalls(String label) {
        return calls.getOrDefault(label, 0L);
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Nonnull
    public Optional<PerformanceRecord> getAvgMeasures(String label) {
        final List<PerformanceRecord> records = measures.get(label);
        if (records != null) {
            double avgTime = 0L;
            double avgCpuTime = 0L;
            double avgUserTime = 0L;
            long l = 1;
            for (PerformanceRecord record : records) {
                avgTime = avg(avgCpuTime, record.getTime(), l);
                avgCpuTime = avg(avgCpuTime, record.getCpuTime(), l);
                avgUserTime = avg(avgCpuTime, record.getUserTime(), l);
                l++;
            }
            return Optional.of(new PerformanceRecord(label,
                    (long) avgTime, (long) avgCpuTime, (long) avgUserTime));
        }
        return Optional.empty();
    }

    private static void filOverallMetrics(PerformanceMeasurer avgMeasurer,
                                          List<PerformanceMeasurer> performanceMeasurers) {
        final Map<String, List<PerformanceRecord>> allData = new HashMap<>
                (performanceMeasurers.size(), 0.75F);
        final Map<String, Long> overallCalls = new HashMap<>(performanceMeasurers.size(), 0.75F);
        for (PerformanceMeasurer measurer : performanceMeasurers) {
            mergeInMeasures(allData, measurer.measures);
            mergeInCalls(overallCalls, measurer.calls);
        }
        final PerformanceMeasurer allDataMeasurer = new PerformanceMeasurer();
        allDataMeasurer.measures.putAll(allData);
        allData.keySet().forEach(key -> {
            avgMeasurer.calls.put(key, overallCalls.get(key));
            avgMeasurer.measures.put(key,
                    Collections.singletonList(allDataMeasurer.getAvgMeasures(key).get()));
        });
    }

    private static void mergeInMeasures(Map<String, List<PerformanceRecord>> allData,
                                        Map<String, List<PerformanceRecord>> measures) {
        measures.keySet()
                .forEach(key -> {
                    if (allData.containsKey(key)) {
                        allData.get(key).addAll(measures.get(key));
                    } else {
                        allData.put(key, new ArrayList<>(measures.get(key)));
                    }
                });
    }

    private static void mergeInCalls(Map<String, Long> overallCalls, Map<String, Long> calls) {
        calls.keySet()
                .forEach(key -> {
                    if (overallCalls.containsKey(key)) {
                        overallCalls.put(key, overallCalls.get(key) + calls.get(key));
                    } else {
                        overallCalls.put(key, calls.get(key));
                    }
                });
    }


    private static double avg(double avgOld, long newValue, long alreadyValuesAdded) {
        return (newValue != 0L) ?
                (((avgOld * (alreadyValuesAdded - 1)) + newValue) / alreadyValuesAdded)
                : avgOld;
    }

    private void registerMeasurement(String label, PerformanceRecord performanceRecord) {
        final List<PerformanceRecord> entry = measures.getOrDefault(label,
                new CopyOnWriteArrayList<>());
        entry.add(performanceRecord);
        measures.put(label, entry);

        final long callsEntry = calls.getOrDefault(label, 0L);
        calls.put(label, callsEntry + 1);
    }

    private long getCpuTimeNano() {
        return recordMXBeanTimes ?
                ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() : 0L;
    }

    private long getUserTimeNano() {
        return recordMXBeanTimes ?
                ManagementFactory.getThreadMXBean().getCurrentThreadUserTime() : 0L;
    }
}

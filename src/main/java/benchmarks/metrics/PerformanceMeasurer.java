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

package benchmarks.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
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
    private final boolean recordMXBeanTimes; // sometimes you need to off it, performance issue

    public PerformanceMeasurer() {
        this(false);
    }

    public PerformanceMeasurer(boolean recordMXBeanTimes) {
        this.recordMXBeanTimes = recordMXBeanTimes;
    }

    @Nonnull
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
        checkMeasure(performanceRecord.getTime());
        registerMeasurement(label, performanceRecord);
        return result;
    }

    public long getCalls(String label) {
        return calls.getOrDefault(label, 0L);
    }

    @SuppressWarnings({"NumericCastThatLosesPrecision", "FeatureEnvy"})
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
            checkMeasure(avgTime);
            return Optional.of(new PerformanceRecord(label,
                    (long) avgTime, (long) avgCpuTime, (long) avgUserTime));
        }
        return Optional.empty();
    }

    private static void checkMeasure(Number time) {
        if (time.floatValue() <= 0.0F) {
            log.warn("Suspicious measure {}!", time);
        }
    }

    private static double avg(double avgOld, long newValue, long alreadyValuesAdded) {
        return (newValue != 0L) ?
                (((avgOld * (alreadyValuesAdded - 1)) + newValue) / alreadyValuesAdded)
                : avgOld;
    }

    void putAllMeasures(Map<String, List<PerformanceRecord>> allData) {
        measures.putAll(allData);
    }

    void putCalls(String key, Long value) {
        calls.put(key, value);
    }

    void putMeasure(String label, List<PerformanceRecord> performanceRecords) {
        measures.put(label, performanceRecords);
    }

    @Nonnull
    Map<String, List<PerformanceRecord>> getMeasures() {
        return measures;
    }

    @Nonnull
    Map<String, Long> getCalls() {
        return calls;
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

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
public final class PerformanceMeasuresCompiler {

    private PerformanceMeasuresCompiler() { /* utility class */ }

    public static PerformanceMeasurer compileOverall(Collection<PerformanceMeasurer>
                                                             performanceMeasurers) {
        final PerformanceMeasurer overallMeasurer = new PerformanceMeasurer();
        filOverallMetrics(overallMeasurer, performanceMeasurers);
        return overallMeasurer;
    }

    @SuppressWarnings("FeatureEnvy")
    private static void filOverallMetrics(PerformanceMeasurer avgMeasurer,
                                          Collection<PerformanceMeasurer> performanceMeasurers) {
        final Map<String, List<PerformanceRecord>> allData = new HashMap<>
                (performanceMeasurers.size(), 0.75F);
        final Map<String, Long> overallCalls = new HashMap<>(performanceMeasurers.size(), 0.75F);
        for (PerformanceMeasurer measurer : performanceMeasurers) {
            mergeInMeasures(allData, measurer.getMeasures());
            mergeInCalls(overallCalls, measurer.getCalls());
        }
        final PerformanceMeasurer allDataMeasurer = new PerformanceMeasurer();
        allDataMeasurer.putAllMeasures(allData);
        allData.keySet().forEach(key -> {
            avgMeasurer.putCalls(key, overallCalls.get(key));
            allDataMeasurer.getAvgMeasures(key).ifPresent(record ->
                    avgMeasurer.putMeasure(key, Collections.singletonList(record)));
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
}

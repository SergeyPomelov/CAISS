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

package benchmarks.ants;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import benchmarks.matrixes.metrics.PerformanceMeasurer;
import benchmarks.matrixes.metrics.PerformanceRecord;

/**
 * @author Sergey Pomelov on 10/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
final class ColonyRunResult {

    private static final Logger log = LoggerFactory.getLogger(ColonyRunResult.class);

    @Nonnegative
    private final long result;
    @Nonnegative
    private final int colonies;
    @Nonnegative
    private final int ants;
    @Nonnegative
    private final long antRuns;
    @Nonnegative
    private final long exchanges;
    @Nonnegative
    private final long avgInitialTrailNs;
    @Nonnegative
    private final long avgAntsRunNs;
    @Nonnegative
    private final long avgExchangeNs;

    private ColonyRunResult(long result, int colonies, int ants, long antRuns,
                            long exchanges, long avgInitialTrailNs,
                            long avgAntsRunNs, long avgExchangeNs) {
        this.result = result;
        this.colonies = colonies;
        this.ants = ants;
        this.antRuns = antRuns;
        this.exchanges = exchanges;
        this.avgInitialTrailNs = avgInitialTrailNs;
        this.avgAntsRunNs = avgAntsRunNs;
        this.avgExchangeNs = avgExchangeNs;
    }

    ColonyRunResult(@Nonnegative long result, @Nonnegative int colonies, @Nonnegative int ants,
                    PerformanceMeasurer colonyRecorder, PerformanceMeasurer antsRecorder) {
        this(result, colonies, ants, antsRecorder.getCalls("runAnt"),
                colonyRecorder.getCalls("exchange"),
                getElapsedTimeOrZero(colonyRecorder, "initialTrail"),
                getElapsedTimeOrZero(antsRecorder, "runAnt"),
                getElapsedTimeOrZero(colonyRecorder, "exchange"));
    }

    public String info() {
        return "runs: " + antRuns +
                ", exch: " + exchanges +
                ", avgRun: " + avgAntsRunNs +
                "ns, avgExch: " + avgExchangeNs + "ns";
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("result", result)
                .add("colonies", colonies)
                .add("ants", ants)
                .add("antRuns", antRuns)
                .add("exchanges", exchanges)
                .add("avgInitialTrailNs", avgInitialTrailNs)
                .add("avgAntsRunNs", avgAntsRunNs)
                .add("avgExchangeNs", avgExchangeNs)
                .toString();
    }

    @Nonnegative
    public int getAnts() {
        return ants;
    }

    private static long getElapsedTimeOrZero(PerformanceMeasurer recorder, String label) {
        final Optional<PerformanceRecord> optional = recorder.getAvgMeasures(label);
        if (optional.isPresent()) {
            return optional.get().getTime();
        } else {
            return 0L;
        }
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    static ColonyRunResult compileOverallResults(Collection<ColonyRunResult> colonyRunResults,
                                                 int colonies,
                                                 int ants) {
        long result = Long.MAX_VALUE;
        double antRuns = 0L;
        double exchanges = 0L;
        double avgInitialTrailNs = 0L;
        double avgAntsRunNs = 0L;
        double avgExchangeNs = 0L;

        final int size = colonyRunResults.size();
        if (size > 0) {
            int i = 1;
            for (ColonyRunResult runResult : colonyRunResults) {
                if (isCompatibleTask(colonies, ants, runResult)) {
                    if (runResult.result < result) {
                        result = runResult.result;
                    }
                    antRuns += runResult.antRuns;
                    exchanges += runResult.exchanges;
                    avgInitialTrailNs = avg(avgInitialTrailNs, runResult.avgInitialTrailNs, i);
                    avgAntsRunNs = avg(avgAntsRunNs, runResult.avgAntsRunNs, i);
                    avgExchangeNs = avg(avgExchangeNs, runResult.avgExchangeNs, i);
                    i++;
                }
            }
        }

        return new ColonyRunResult(result, colonies, ants, (long) antRuns, (long) exchanges,
                (long) avgInitialTrailNs, (long) avgAntsRunNs, (long) avgExchangeNs);
    }

    private static boolean isCompatibleTask(int colonies, int ants, ColonyRunResult runResult) {
        if ((runResult.colonies != colonies) || (runResult.ants != ants) ||
                (runResult.antRuns <= 0L) || (runResult.avgAntsRunNs == 0L) ||
                (runResult.result == Long.MAX_VALUE)) {
            log.warn("Averaging not matching or valid task, ignored. Colonies: {}. Ants: {}. " +
                    "Task to average {}.", colonies, ants, runResult);
            return false;
        }
        return true;
    }

    private static double avg(double avgOld, double newValue, double alreadyValuesAdded) {
        return ((avgOld * (alreadyValuesAdded - 1)) + newValue) / alreadyValuesAdded;
    }

    @Nonnegative
    long getAntRuns() {
        return antRuns;
    }

    @Nonnegative
    long getExchanges() {
        return exchanges;
    }

    @Nonnegative
    int getColonies() {
        return colonies;
    }

    @Nonnegative
    long getResult() {
        return result;
    }

    @Nonnegative
    long getAvgInitialTrailNs() {
        return avgInitialTrailNs;
    }

    @Nonnegative
    long getAvgAntsRunNs() {
        return avgAntsRunNs;
    }

    @Nonnegative
    long getAvgExchangeNs() {
        return avgExchangeNs;
    }
}

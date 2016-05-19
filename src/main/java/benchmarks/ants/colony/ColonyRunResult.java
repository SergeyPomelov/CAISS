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

package benchmarks.ants.colony;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import benchmarks.metrics.PerformanceMeasurer;
import benchmarks.metrics.PerformanceRecord;

/**
 * @author Sergey Pomelov on 10/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class ColonyRunResult {

    private static final Logger log = LoggerFactory.getLogger(ColonyRunResult.class);

    @Nonnull
    private final String id;
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

    public ColonyRunResult(String id, long result, int colonies, int ants, long antRuns,
                           long exchanges, long avgInitialTrailNs,
                           long avgAntsRunNs, long avgExchangeNs) {
        this.id = id;
        this.result = result;
        this.colonies = colonies;
        this.ants = ants;
        this.antRuns = antRuns;
        this.exchanges = exchanges;
        this.avgInitialTrailNs = avgInitialTrailNs;
        this.avgAntsRunNs = avgAntsRunNs;
        this.avgExchangeNs = avgExchangeNs;
        if (!isValid()) {
            log.warn("Not valid result {}.", toString());
        }
    }

    ColonyRunResult(String id, @Nonnegative long result, @Nonnegative int colonies,
                    @Nonnegative int ants, PerformanceMeasurer colonyRecorder,
                    PerformanceMeasurer antsRecorder) {
        this(id, result, colonies, ants, antsRecorder.getCalls("runAnt"),
                colonyRecorder.getCalls("exchange"),
                getElapsedTimeOrZero(colonyRecorder, "initialTrail"),
                getElapsedTimeOrZero(antsRecorder, "runAnt"),
                getElapsedTimeOrZero(colonyRecorder, "exchange"));
    }

    @Nonnull
    public String info() {
        return "Id: " + id +
                ", runs: " + antRuns +
                ", exch: " + exchanges +
                ", avgRun: " + avgAntsRunNs +
                "ns, avgExch: " + avgExchangeNs + "ns";
    }

    @Nonnegative
    public int getAnts() {
        return ants;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    public boolean isValid(int desiredColonies, int desiredAnts) {
        return (desiredColonies == colonies) && (desiredAnts == ants) && isValid();
    }

    private boolean isValid() {
        return (antRuns > 0L) && (avgAntsRunNs > 0L) && (result != Long.MAX_VALUE);
    }

    private static long getElapsedTimeOrZero(PerformanceMeasurer recorder, String label) {
        final Optional<PerformanceRecord> optional = recorder.getAvgMeasures(label);
        return optional.isPresent() ? optional.get().getTime() : 0L;
    }

    @Nonnegative
    public long getAntRuns() {
        return antRuns;
    }

    @Nonnegative
    public long getExchanges() {
        return exchanges;
    }

    @Nonnegative
    public int getColonies() {
        return colonies;
    }

    @Nonnegative
    public long getResult() {
        return result;
    }

    @Nonnegative
    public long getAvgInitialTrailNs() {
        return avgInitialTrailNs;
    }

    @Nonnegative
    public long getAvgAntsRunNs() {
        return avgAntsRunNs;
    }

    @Nonnegative
    public long getAvgExchangeNs() {
        return avgExchangeNs;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
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
}

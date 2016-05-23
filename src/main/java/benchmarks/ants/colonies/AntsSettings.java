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

package benchmarks.ants.colonies;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.data.IDistancesData;
import benchmarks.ants.data.TSPDistanceData;
import javafx.util.Pair;
import util.TimeUtil;

import static util.Constants.FS;

/**
 * Package-local constants and ACO settings holder.
 *
 * @author Sergey Pomelov on 28/04/2016.
 */
@Immutable
public final class AntsSettings implements Serializable {

    private static final long serialVersionUID = -345544272953782575L;

    @Nonnegative
    private final int optimum;
    @Nonnegative
    private final long runPeriodNanos;
    @Nonnegative
    private final long exchangeNanos;
    @Nonnegative
    private final float evaporationCoefficient;
    @Nonnegative
    private final float initialTrail;
    @Nonnull
    private final IDistancesData graph;

    public AntsSettings(Pair<Integer, String> data, float evaporation, int secondsToRun) {
        this(data.getKey(), data.getValue(), TimeUtil.secToNano(secondsToRun),
                TimeUtil.mlsToNano(1000), evaporation, 1.0F);
    }

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public AntsSettings(@Nonnegative int optimum, @Nonnull String file,
                        @Nonnegative long runPeriodNanos,
                        @Nonnegative long exchangeNanos,
                        @Nonnegative float evaporationCoefficient,
                        @Nonnegative float initialTrail) {
        this.optimum = optimum;
        this.runPeriodNanos = runPeriodNanos;
        this.exchangeNanos = exchangeNanos;
        this.evaporationCoefficient = evaporationCoefficient;
        this.initialTrail = initialTrail;
        graph = new TSPDistanceData(FS + "build" + FS + "resources" + FS + "main"
                + FS + "tsp_data" + FS + file + ".tsp");
    }

    @Nonnegative
    public int getOptimum() {
        return optimum;
    }

    @Nonnegative
    long getRunPeriodNanos() {
        return runPeriodNanos;
    }

    @Nonnegative
    public long getExchangeNanos() {
        return exchangeNanos;
    }

    @Nonnegative
    public float getEvaporationCoefficient() {
        return evaporationCoefficient;
    }

    @Nonnegative
    public float getInitialTrail() {
        return initialTrail;
    }

    @Nonnull
    public IDistancesData getGraph() {
        return graph;
    }
}

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

import com.google.common.annotations.VisibleForTesting;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.data.IDistancesData;
import benchmarks.ants.data.TSPDistanceData;
import util.TimeUtil;

import static util.Constants.FS;

/**
 * Package-local constants and ACO settings holder.
 *
 * @author Sergey Pomelov on 28/04/2016.
 */
@SuppressWarnings("MagicNumber")
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
    private final String file; // wi29
    @Nonnull
    private final IDistancesData graph;

    public AntsSettings() {
        this(9352, TimeUtil.secToNano(600), TimeUtil.mlsToNano(1000), 0.1F, 1.0F, "qa194");
    }

    @VisibleForTesting
    public AntsSettings(@Nonnegative int optimum, @Nonnegative long runPeriodNanos,
                        @Nonnegative long exchangeNanos, @Nonnegative float evaporationCoefficient,
                        @Nonnegative float initialTrail, @Nonnull String file) {
        this.optimum = optimum;
        this.runPeriodNanos = runPeriodNanos;
        this.exchangeNanos = exchangeNanos;
        this.evaporationCoefficient = evaporationCoefficient;
        this.initialTrail = initialTrail;
        this.file = file;
        graph = new TSPDistanceData(FS + "build" + FS + "resources" + FS + "main"
                + FS + "tsp_data" + FS + file + ".tsp");
    }

    @Nonnegative
    public int getOptimum() {
        return optimum;
    }

    @Nonnegative
    public long getRunPeriodNanos() {
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

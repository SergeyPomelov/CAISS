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

package benchmarks.ants.presets;

import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.util.Pair;

public final class AntsExperimentSeriesPresetBuilder {
    private static final float DEFAULT_EVAPORATION = 0.1F;
    private Pair<Integer, String> data = null;
    private List<Integer> colonies = null;
    private List<Integer> ants = null;
    private int runsForAverageResult;
    private int overallRunTimeInMinutes;
    private long overallRunTimeInNanos;

    @VisibleForTesting
    public AntsExperimentSeriesPresetBuilder setData(Pair<Integer, String> data) {
        this.data = data;
        return this;
    }

    @VisibleForTesting
    public AntsExperimentSeriesPresetBuilder setColonies(List<Integer> colonies) {
        this.colonies = colonies;
        return this;
    }

    @VisibleForTesting
    public AntsExperimentSeriesPresetBuilder setAnts(List<Integer> ants) {
        this.ants = ants;
        return this;
    }

    @VisibleForTesting
    public AntsExperimentSeriesPresetBuilder setRunsForAverageResult(int runsForAverageResult) {
        this.runsForAverageResult = runsForAverageResult;
        return this;
    }

    @SuppressWarnings("unused")
    @VisibleForTesting
    public AntsExperimentSeriesPresetBuilder setOverallRunTimeInNanos(long overallRunTimeInNanos) {
        this.overallRunTimeInNanos = overallRunTimeInNanos;
        return this;
    }

    @SuppressWarnings("unused")
    public long getOverallRunTimeInNanos() {
        return overallRunTimeInNanos;
    }

    AntsExperimentSeriesPresetBuilder setOverallRunTimeInMinutes(int overallRunTimeInMinutes) {
        this.overallRunTimeInMinutes = overallRunTimeInMinutes;
        return this;
    }

    public AntsExperimentSeriesPreset createAntsExperimentPreset() throws IOException {
        return new AntsExperimentSeriesPreset(data, colonies, ants, DEFAULT_EVAPORATION,
                runsForAverageResult,
                TimeUnit.MINUTES.toNanos(overallRunTimeInMinutes));
    }
}
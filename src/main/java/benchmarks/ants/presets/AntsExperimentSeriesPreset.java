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

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colony.CachedRawEdgeQualities;
import javafx.util.Pair;
import util.Restrictions;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
// all collection fields under immutable implementations
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
public final class AntsExperimentSeriesPreset implements Serializable {

    private static final long serialVersionUID = -8708406567348424308L;

    @Nonnull
    private final Pair<Integer, String> data;
    @Nonnull
    private final List<Integer> colonies;
    @Nonnull
    private final List<Integer> ants;
    @Nonnull
    private final int runsForAverageResult;
    @Nonnull
    private final long overallRunTimeInNanos;
    @Nonnull
    private final AntsSettings settings;
    @Nonnull
    private final CachedRawEdgeQualities qualities;

    /**
     * Use the constructors only in lazy style.
     *
     * @see AntsExperimentSeriesPresetBuilder
     */
    @SuppressWarnings("SameParameterValue")
    AntsExperimentSeriesPreset(Pair<Integer, String> data, List<Integer> colonies,
                               List<Integer> ants, float evaporation, int runsForAverageResult,
                               long overallRunTimeInNanos) {
        Restrictions.ifContainsNullFastFail(data, colonies, ants);
        Restrictions.ifNotOnlyPositivesFastFail(runsForAverageResult, overallRunTimeInNanos);
        this.data = data;
        this.colonies = ImmutableList.copyOf(colonies);
        this.ants = ImmutableList.copyOf(ants);
        this.runsForAverageResult = runsForAverageResult;
        this.overallRunTimeInNanos = overallRunTimeInNanos;
        settings = new AntsSettings(data, evaporation, calculateSecondsToRun());
        qualities = new CachedRawEdgeQualities(settings.getGraph());
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private int calculateSecondsToRun() {
        return (int) (((float) TimeUnit.NANOSECONDS.toSeconds(overallRunTimeInNanos)) /
                (runsForAverageResult * ants.size() * colonies.size()));
    }

    @Nonnull
    public Pair<Integer, String> getData() {
        return data;
    }

    @Nonnull
    public Iterable<Integer> getColonies() {
        return colonies;
    }

    @Nonnull
    public Iterable<Integer> getAnts() {
        return ants;
    }

    @Nonnull
    public int getRunsForAverageResult() {
        return runsForAverageResult;
    }

    @Nonnull
    public AntsSettings getSettings() {
        return settings;
    }

    @Nonnull
    public CachedRawEdgeQualities getQualities() {
        return qualities;
    }
}

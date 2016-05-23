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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.colony.CachedRawEdgeQualities;
import benchmarks.ants.presets.AntsExperimentSeriesPreset;

import static util.Restrictions.ifNegativeFail;
import static util.Restrictions.ifNullFail;

/**
 * @author Sergey Pomelov on 21/05/2016.
 */
@SuppressWarnings("FeatureEnvy")
@Immutable
public final class AntsExperimentData {

    @Nonnegative
    private final int colonies;
    @Nonnegative
    private final int ants;
    @Nonnull
    private final AntsExperimentSeriesPreset preset;

    public AntsExperimentData(int colonies, int ants, AntsExperimentSeriesPreset preset) {
        this.colonies = ifNegativeFail(colonies);
        this.ants = ifNegativeFail(ants);
        this.preset = ifNullFail(preset);
    }

    @Nonnegative
    public int getColonies() {
        return colonies;
    }

    @Nonnegative
    public int getAnts() {
        return ants;
    }

    @Nonnull
    public AntsExperimentSeriesPreset getPreset() {
        return preset;
    }

    @Nonnull
    AntsSettings getSettings() {
        return preset.getSettings();
    }

    @Nonnull
    CachedRawEdgeQualities getQualities() {
        return preset.getQualities();
    }
}

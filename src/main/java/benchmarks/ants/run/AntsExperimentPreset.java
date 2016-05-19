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

package benchmarks.ants.run;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.AntsSettings;
import javafx.util.Pair;
import util.Restrictions;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
// all collection fields under immutable implementations
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
final class AntsExperimentPreset implements Serializable {

    private static final int SEC_IN_MINUTE = 60;
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
    private final int overallRunTimeInMinutes;
    @Nonnull
    private final AntsSettings settings;

    AntsExperimentPreset(Pair<Integer, String> data, List<Integer> colonies,
                         List<Integer> ants, int runsForAverageResult,
                         int overallRunTimeInMinutes) {
        Restrictions.ifContainsNullFastFail(data, colonies, ants);
        Restrictions.ifNotOnlyPositivesFastFail(runsForAverageResult, overallRunTimeInMinutes);
        this.data = data;
        this.colonies = ImmutableList.copyOf(colonies);
        this.ants = ImmutableList.copyOf(ants);
        this.runsForAverageResult = runsForAverageResult;
        this.overallRunTimeInMinutes = overallRunTimeInMinutes;
        settings = new AntsSettings(data, calculateSecondsToRun());
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private int calculateSecondsToRun() {
        return (int) (((float) overallRunTimeInMinutes * SEC_IN_MINUTE) /
                (runsForAverageResult * ants.size() * colonies.size()));
    }

    @Nonnull
    Pair<Integer, String> getData() {
        return data;
    }

    @Nonnull
    Iterable<Integer> getColonies() {
        return colonies;
    }

    @Nonnull
    Iterable<Integer> getAnts() {
        return ants;
    }

    @Nonnull
    int getRunsForAverageResult() {
        return runsForAverageResult;
    }

    @Nonnull
    int getOverallRunTimeInMinutes() {
        return overallRunTimeInMinutes;
    }

    @Nonnull
    AntsSettings getSettings() {
        return settings;
    }
}

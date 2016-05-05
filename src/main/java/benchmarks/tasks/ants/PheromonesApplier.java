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

package benchmarks.tasks.ants;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.tasks.ants.ant.AntRunResult;

import static benchmarks.tasks.ants.AntsSettings.EVAPORATION_COEFFICIENT;

/**
 * @author Sergey Pomelov on 04/05/2016.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
final class PheromonesApplier {

    private PheromonesApplier() { /* package local utility class*/ }

    static void applyPheromones(AntRunResult runResult, float[][] trailsToChange) {
        final int trailLength = trailsToChange.length;
        for (int i = 0; i < trailLength; i++) {
            for (int j = 0; j < i; j++) {
                float t = ((1.0F - EVAPORATION_COEFFICIENT) * trailsToChange[i][j])
                        + runResult.getPheromonesDelta()[i][j]
                        + runResult.getPheromonesDelta()[j][i];
                trailsToChange[i][j] = t;
                trailsToChange[j][i] = t;
            }
        }
    }
}

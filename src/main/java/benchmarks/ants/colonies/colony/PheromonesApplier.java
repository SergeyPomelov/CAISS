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

package benchmarks.ants.colonies.colony;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colonies.colony.ant.AntRunResult;

/**
 * Class apply pheromone trails changes according to the ant's run results.
 * @author Sergey Pomelov on 04/05/2016.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
final class PheromonesApplier {

    private PheromonesApplier() { /* package local utility class*/ }

    static void applyPheromones(AntRunResult runResult, AntsSettings settings,
                                float[][] trailsToChange) {
        final int trailLength = trailsToChange.length;
        final float evaporationMultiplier = (1.0F - settings.getEvaporationCoefficient());
        for (int i = 0; i < trailLength; i++) {
            final float passedVertexPheromonesDelta = runResult.getPheromonesDelta().getDelta(i);
            final int passedVertexPheromonesRowIdx =
                    runResult.getPheromonesDelta().getDeltaColumnIdx(i);
            for (int j = 0; j < trailLength; j++) {
                trailsToChange[i][j] *= evaporationMultiplier;
                if (passedVertexPheromonesRowIdx == j) {
                    trailsToChange[i][j] += passedVertexPheromonesDelta;
                }
            }
        }
    }
}

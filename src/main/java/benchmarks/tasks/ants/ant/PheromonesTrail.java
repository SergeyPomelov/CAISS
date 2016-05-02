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

package benchmarks.tasks.ants.ant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Class for spray pheromones trail along the route.
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@ParametersAreNonnullByDefault
final class PheromonesTrail {

    @Nonnull
    private final double[][] trailDelta;

    PheromonesTrail(@Nonnegative int size) {
        trailDelta = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trailDelta[i][j] = 0;
            }
        }
    }

    @Nonnull
    double[][] generateTrail(int[] tour, @Nonnegative double amount) {
        int from = tour[0];
        for (final int vertex : tour) { // iterates through the tour
            final int destination = from;
            from = vertex;
            trailDelta[from][destination] += amount; // spraying the trail delta
        }
        //noinspection ReturnOfCollectionOrArrayField, while pacjage local this lack of
        return trailDelta; // encapsulation is ok
    }

    @Nonnull
    double[][] getTrailDelta() {
        //noinspection ReturnOfCollectionOrArrayField, while it used package-local it's ok.
        return trailDelta;
    }
}

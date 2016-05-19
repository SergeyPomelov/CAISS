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

package benchmarks.ants.colony.ant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import benchmarks.ants.colony.ant.Array2DFloatDelta.Builder;

import static util.Restrictions.ifNullFail;

/**
 * Class for spray pheromones trail along the route.
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
final class PheromonesTrail {

    private Array2DFloatDelta trailsDelta = null;

    PheromonesTrail() {
    }

    @Nonnull
    void generateTrail(int[] tour, @Nonnegative float amount) {
        final Builder builder = new Builder(tour.length);
        int from = tour[0];
        for (final int vertex : tour) { // iterates through the tour
            final int destination = from;
            from = vertex;
            builder.add(from, destination, amount); // spraying the trail delta
        }
        trailsDelta = builder.build();
    }

    @Nonnull
    Array2DFloatDelta getTrailsDelta() {
        //noinspection ReturnOfCollectionOrArrayField, while it used package-local it's ok.
        return ifNullFail(trailsDelta);
    }
}

/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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

import java.security.SecureRandom;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.tasks.ants.data.IDistancesData;

import static benchmarks.tasks.ants.ant.RouteFinder.findNextVertex;

/**
 * @author Sergey Pomelov on 29/04/2016.
 * @see RunningAnt
 */
@ParametersAreNonnullByDefault
final class TourBuilder {
    private static final Random rand = new SecureRandom();

    @Nonnull
    private final IDistancesData graphMatrix;
    @Nonnull
    private final int[] tour;
    @Nonnull
    private final int[] allowedVertexes;
    @Nonnull
    private final boolean[] visited;
    @Nonnull
    private final double[][] vertexQualities;
    @Nonnegative
    private final int size;

    TourBuilder(IDistancesData graphMatrix, double[][] trail) {
        this.graphMatrix = graphMatrix;

        size = graphMatrix.getSize();
        tour = new int[size];
        allowedVertexes = new int[size];
        visited = new boolean[size];
        vertexQualities = new double[size][size];

        initPheromones(trail);
    }

    private void initPheromones(double[][] trail) {
        for (int i = 0; i < size; i++) {
            visited[i] = false;
            for (int j = 0; j < size; j++) {
                vertexQualities[i][j] = StrictMath.pow(graphMatrix.getDist(i, j), -1.0)
                        * trail[i][j];
            }
        }
    }

    TourData buildUncycledTour() {
        long tourLength = 0L;
        int startVertex = rand.nextInt(tour.length);

        tour[0] = startVertex;
        visited[startVertex] = true;

        boolean success = true;
        for (int i = 1; (i < size) && success; i++) {
            final Integer destinationIndex = findNextVertex(startVertex, size, vertexQualities,
                    visited, allowedVertexes);
            if (destinationIndex == null) {
                tourLength = Integer.MAX_VALUE;
                success = false;
            } else {
                final int dst = allowedVertexes[destinationIndex];
                visited[dst] = true;
                tourLength += graphMatrix.getDist(startVertex, dst);
                tour[i] = dst;
                startVertex = dst;
            }
        }
        return new TourData(success, tour, tourLength);
    }
}

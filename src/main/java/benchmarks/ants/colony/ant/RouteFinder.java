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

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.ants.colony.CachedRawEdgeQualities;
import benchmarks.ants.data.IDistancesData;

/**
 * Implements the ant's decision about the next vertex to go.
 *
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@ParametersAreNonnullByDefault
final class RouteFinder {

    private static final float MIN_VERTEX_QUALITY = Float.MIN_VALUE * 10.0E2F;

    private RouteFinder() { /* utility package-local class */ }

    @SuppressWarnings("MethodCanBeVariableArityMethod") // by design
    @Nonnull
    @Nonnegative
    static Optional<Integer> findNextVertex(@Nonnegative int startVertex,
                                            float[][] trail, boolean[] visited,
                                            int[] allowedVertexes, IDistancesData data,
                                            CachedRawEdgeQualities cachedRawEdgeQualities) {
        final int size = data.getSize();
        int possibleVertexesToGo = 0;
        float totalWeight = 0;
        final float[] weights = new float[size];
        for (int j = 0; j < size; j++) {
            if (!visited[j]) {
                final float vertexQuality =
                        getTrail(cachedRawEdgeQualities.getDist(startVertex, j) *
                                trail[startVertex][j]);
                totalWeight += vertexQuality;
                weights[possibleVertexesToGo] = totalWeight;
                allowedVertexes[possibleVertexesToGo] = j;
                possibleVertexesToGo++;
            }
        }

        if (totalWeight == 0) {
            totalWeight = 0;
        }

        return Optional.ofNullable((totalWeight == 0) ?
                null : calculateDestination(totalWeight, possibleVertexesToGo, weights));
    }

    private static float getTrail(float value) {
        return (value == 0.0F) ? MIN_VERTEX_QUALITY : value;
    }

    @SuppressWarnings("MethodCanBeVariableArityMethod") // by design
    private static int calculateDestination(float totalWeight, int possibleVertexesToGo,
                                            float[] weights) {
        final float pointer = generatePointer(totalWeight);
        for (int i = 0; i < possibleVertexesToGo; i++) {
            if (isLessOrEqual(pointer, weights[i])) {
                return i;
            }
        }
        return possibleVertexesToGo;
    }

    private static float generatePointer(float totalWeight) {
        return totalWeight * ThreadLocalRandom.current().nextFloat();
    }

    private static boolean isLessOrEqual(float pointer, float weight) {
        return pointer <= weight;
    }
}

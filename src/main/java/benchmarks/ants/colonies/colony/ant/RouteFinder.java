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

package benchmarks.ants.colonies.colony.ant;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import benchmarks.ants.colonies.colony.CachedRawEdgeQualities;
import benchmarks.ants.data.IDistancesData;

/**
 * Implements the ant's decision about the next vertex to go.
 *
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
final class RouteFinder {

    private static final float MIN_VERTEX_QUALITY = Float.MIN_VALUE * 10.0E2F;

    private final int size;
    private final float[][] edgeQualities;

    RouteFinder(IDistancesData data, CachedRawEdgeQualities cachedRawEdgeQualities) {
        size = data.getSize();
        edgeQualities = cachedRawEdgeQualities.getEdgesQualities();
    }

    @SuppressWarnings("MethodCanBeVariableArityMethod") // by design
    @Nonnull
    @Nonnegative
    Optional<Integer> findNextVertex(@Nonnegative int startVertex, float[][] trail,
                                     boolean[] visited, int[] allowedVertexes) {
        final float[] weights = new float[size];

        int possibleVertexesToGo = 0;
        float totalWeight = 0;
        for (int j = 0; j < size; j++) {
            if (!visited[j]) {
                final float vertexQuality =
                        Math.max(edgeQualities[startVertex][j] * trail[startVertex][j],
                                MIN_VERTEX_QUALITY);

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

    @SuppressWarnings("MethodCanBeVariableArityMethod") // by design
    private static int calculateDestination(float totalWeight, int possibleVertexesToGo,
                                            float[] weights) {
        final float pointer = generatePointer(totalWeight);
        for (int i = 0; i < possibleVertexesToGo; i++) {
            if (pointer <= weights[i]) {
                return i;
            }
        }
        return possibleVertexesToGo;
    }

    private static float generatePointer(float totalWeight) {
        return totalWeight * ThreadLocalRandom.current().nextFloat();
    }
}

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

import java.security.SecureRandom;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Implements the ant's decision about the next vertex to go.
 *
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@ParametersAreNonnullByDefault
final class RouteFinder {

    private static final Random rand = new SecureRandom();
    private static final double COMP_MAX_ERR = Double.MIN_VALUE * 10.0E-3D;

    private RouteFinder() { /* utility package-local class */ }

    @SuppressWarnings("MethodCanBeVariableArityMethod") // by design
    @Nullable
    @Nonnegative
    static Integer findNextVertex(@Nonnegative int startVertex, @Nonnegative int size,
                                  double[][] vertexQualities, boolean[] visited,
                                  int[] allowedVertexes) {
        int possibleVertexesToGo = 0;
        double totalWeight = 0;
        final double[] weights = new double[size];
        for (int j = 0; j < size; j++) {
            boolean success = true;
            final double vertexQuality = vertexQualities[startVertex][j];
            if (visited[j] || (vertexQuality < COMP_MAX_ERR)) {
                success = false;
            }
            if (success) {
                totalWeight += vertexQuality;
                weights[possibleVertexesToGo] = totalWeight;
                allowedVertexes[possibleVertexesToGo] = j;
                possibleVertexesToGo++;
            }
        }

        return (totalWeight == 0) ?
                null : calculateDestination(totalWeight, possibleVertexesToGo, weights);
    }

    @SuppressWarnings("MethodCanBeVariableArityMethod")
    private static int calculateDestination(double totalWeight, int possibleVertexesToGo,
                                            double[] weights) {
        int result = 0;
        final double val = totalWeight * rand.nextDouble();
        for (int i = 0; i < possibleVertexesToGo; i++) {
            if (weights[i] > val) {
                return i;
            } else {
                result = possibleVertexesToGo;
            }
        }
        return result;
    }
}

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

package benchmarks.tasks.ants;

import java.security.SecureRandom;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

@SuppressWarnings("SameParameterValue")
@Immutable
@ParametersAreNonnullByDefault
final class RunningAnt {
    private static final Random rand = new SecureRandom();
    private static final double COMP_MAX_ERR = 0.000000001;

    @Nonnull
    private final IGraph graphMatrix;
    @Nonnull
    private final int[] tour;
    @Nonnull
    private final int[] allowedVertexes;

    @Nonnull
    private final boolean[] visited;
    @Nonnull
    private final double[][] trailDelta;
    @Nonnull
    private final double[][] vertexQualities;

    @Nonnegative
    private final int size;
    @Nonnull
    private final AntRunResult runResult;
    private final double avgLen;
    private final long start = System.currentTimeMillis();

    RunningAnt(IGraph inSquareSymmetricGraph, double[][] inTrail, double inAvgLen) {
        graphMatrix = inSquareSymmetricGraph;
        size = graphMatrix.getSize();
        avgLen = inAvgLen;

        tour = new int[size];
        allowedVertexes = new int[size];
        visited = new boolean[size];
        trailDelta = new double[size][size];
        vertexQualities = new double[size][size];

        for (int i = 0; i < size; i++) {
            visited[i] = false;
            for (int j = 0; j < size; j++) {
                trailDelta[i][j] = 0;
                vertexQualities[i][j] = StrictMath.pow(graphMatrix.getDist(i, j), -1.0)
                        * inTrail[i][j];
            }
        }

        runResult = runAnt();
    }

    @Nonnull
    public AntRunResult getRunResult() {
        return runResult;
    }

    @Nonnull
    private static String printArray(final int... array) {
        final StringBuilder out = new StringBuilder(64);
        for (final int anArray : array) {
            out.append(anArray + 1).append('>');
        }
        return out.toString();
    }

    @Nonnull
    private AntRunResult runAnt() {
        int src;
        long len = 0;

        src = rand.nextInt(tour.length);
        tour[0] = src;
        visited[src] = true;

        boolean success = true;
        for (int i = 1; (i < size) && success; i++) {
            final Integer dstIndex = find(src);
            if (dstIndex == null) {
                len = Integer.MAX_VALUE;
                success = false;
            } else {
                final int dst = allowedVertexes[dstIndex];
                visited[dst] = true;
                len += graphMatrix.getDist(src, dst);
                tour[i] = dst;
                src = dst;
            }
        }

        if (success) {
            final double chg = avgLen / len;
            sprayTrail(chg);
        }

        return new AntRunResult(tour, len, trailDelta,
                '|' + ((len < Long.MAX_VALUE) ? String.valueOf(len) : "failure") + '|'
                        + printArray(tour) + ' ' + (System.currentTimeMillis() - start) + " ms");
    }

    private void sprayTrail(final double amount) {
        int src = tour[0];
        for (final int vertex : tour) { // iterates through the tour
            final int dst = src;
            src = vertex;
            trailDelta[src][dst] += amount;
        }
    }

    @Nullable
    @Nonnegative
    private Integer find(final int inScr) {
        int n = 0; // possible to go vertexes counter
        double totalWeight = 0;
        final double[] weights = new double[size];
        for (int j = 0; j < size; j++) {
            boolean success = true;
            final double vertexQuality = vertexQualities[inScr][j];
            if (visited[j] || (vertexQuality < COMP_MAX_ERR)) {
                success = false;
            }
            if (success) {
                totalWeight += vertexQuality;
                weights[n] = totalWeight;
                allowedVertexes[n] = j;
                n++;
            }
        }

        int result = 0;
        final double val = totalWeight * rand.nextDouble();
        for (int i = 0; i < n; i++) {
            if (weights[i] > val) {
                return i;
            } else {
                result = n;
            }
        }
        return (totalWeight == 0) ? null : result;
    }
}

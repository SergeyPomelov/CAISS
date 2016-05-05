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
import javax.annotation.concurrent.Immutable;

import benchmarks.tasks.ants.data.IDistancesData;

import static benchmarks.tasks.ants.OutputFormat.printTour;

/**
 * An agent of the ACO.
 * @author Sergey Pomelov on 29/04/2016.
 * @see benchmarks.tasks.ants.AntsColony
 */
@SuppressWarnings("SameParameterValue")
@Immutable
@ParametersAreNonnullByDefault
public final class RunningAnt {

    @Nonnull
    private final IDistancesData graph;
    @Nonnull
    private final AntRunResult runResult;
    @Nonnull
    private final TourBuilder tourBuilder;      // tour generation delegate
    @Nonnull
    private final PheromonesTrail trailSpray;   // trail data and generation delegate

    @Nonnegative
    private final long startMls = System.currentTimeMillis();

    public RunningAnt(IDistancesData graph, float[][] trails) {
        this.graph = graph;
        trailSpray = new PheromonesTrail(graph.getSize());
        tourBuilder = new TourBuilder(graph, trails);
        runResult = runAnt();
    }

    @Nonnull
    public AntRunResult getRunResult() {
        return runResult;
    }

    private static boolean checkFinishedTourLength(long finalTourLength) {
        if (finalTourLength <= 0) {
            throw new IllegalStateException("Wrong tour length had been obtained!");
        }
        return finalTourLength < Integer.MAX_VALUE;
    }

    @SuppressWarnings("FeatureEnvy")
    @Nonnull
    private AntRunResult runAnt() {
        final TourData tourData = tourBuilder.buildUncycledTour();
        final int[] tour = tourData.getTour();

        long finalTourLength = Long.MAX_VALUE;
        boolean finalSuccess = tourData.isSuccess();
        if (finalSuccess) {
            finalTourLength = tryToFinishCycle(tour, tourData.getLength());
            finalSuccess = checkFinishedTourLength(finalTourLength);
        }

        final TourData finalTourData = new TourData(finalSuccess, tour, finalTourLength);
        return new AntRunResult(finalTourData, trailSpray.getTrailsDelta(),
                resultToString(finalTourData));
    }

    @SuppressWarnings("FeatureEnvy")
    private String resultToString(TourData tourData) {
        return '|' + ((tourData.getLength() < Long.MAX_VALUE) ?
                String.valueOf(tourData.getLength()) : "failure")
                + '|' + printTour(tourData.getTour()) + ' '
                + (System.currentTimeMillis() - startMls) + " ms";
    }

    @Nonnegative
    private long tryToFinishCycle(int[] tour, long tourLength) {
        final int start = tour[0];
        final int end = tour[tour.length - 1];
        final int lastPathLength = graph.getDist(end, start);
        if (lastPathLength < Integer.MAX_VALUE) {
            final float chg = 1.0F / tourLength;
            trailSpray.generateTrail(tour, chg);
            return tourLength + lastPathLength; // returning to the start point length addition
        } else {
            return Integer.MAX_VALUE;
        }
    }
}

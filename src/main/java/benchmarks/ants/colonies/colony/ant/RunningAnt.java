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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import benchmarks.ants.colonies.colony.AntsColony;
import benchmarks.ants.colonies.colony.CachedRawEdgeQualities;
import benchmarks.ants.data.IDistancesData;
import benchmarks.metrics.PerformanceMeasurer;

import static benchmarks.ants.colonies.colony.OutputFormat.printTour;

/**
 * An agent of the ACO.
 * @author Sergey Pomelov on 29/04/2016.
 * @see AntsColony
 */
@SuppressWarnings("SameParameterValue")
@Immutable
@ParametersAreNonnullByDefault
public final class RunningAnt {

    private static final Logger log = LoggerFactory.getLogger(RunningAnt.class);

    @Nonnull
    private final IDistancesData graph;
    @Nonnull
    private final TourBuilder tourBuilder;      // tour generation delegate
    @Nonnull
    private final PheromonesTrailGenerator trailSpray;   // trail data and generation delegate
    @Nonnull
    private final PerformanceMeasurer performanceMeasurer = new PerformanceMeasurer();
    private final AntRunResult runResult;

    @Nonnegative
    private final long startMls = System.currentTimeMillis();

    public RunningAnt(IDistancesData graph, CachedRawEdgeQualities cachedRawEdgeQualities,
                      float[][] trails) {
        this.graph = graph;
        trailSpray = new PheromonesTrailGenerator();
        tourBuilder = new TourBuilder(graph, cachedRawEdgeQualities, trails);

        AntRunResult result = null;
        try {
            result = performanceMeasurer.measurePerformanceCallable(this::runAnt, "runAnt");
        } catch (Exception e) {
            log.error("Error while obtaining run result!", e);
        }
        runResult = result;
    }

    @Nonnull
    public Optional<AntRunResult> getRunResult() {
        return Optional.ofNullable(runResult);
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
        } else {
            log.info("Ant not succeed. Result: {}. Tour: {}.", tourData.getLength(), tour);
        }

        final TourData finalTourData = new TourData(finalSuccess, tour, finalTourLength);
        return new AntRunResult(finalTourData, trailSpray.getTrailsPheromonesDelta(),
                performanceMeasurer, resultToString(finalTourData));
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
            trailSpray.generateTrailPheromonesDelta(tour, chg);
            return tourLength + lastPathLength; // returning to the start point length addition
        } else {
            return Integer.MAX_VALUE;
        }
    }
}

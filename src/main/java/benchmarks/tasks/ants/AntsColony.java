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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import benchmarks.tasks.ants.ant.AntRunResult;
import benchmarks.tasks.ants.ant.RunningAnt;
import util.Restrictions;

import static benchmarks.tasks.ants.AntsColoniesSettings.EVAPORATION_COEFFICIENT;
import static benchmarks.tasks.ants.AntsColoniesSettings.GRAPH;
import static benchmarks.tasks.ants.AntsColoniesSettings.INITIAL_TRAIL;
import static benchmarks.tasks.ants.AntsColoniesSettings.SIZE;
import static benchmarks.tasks.ants.AntsParallelExecutor.runExecutor;

public final class AntsColony implements IAntsOptimization {

    private static final long serialVersionUID = 1858529504894436119L;
    private static final Logger log = LoggerFactory.getLogger(AntsColony.class);

    private final double[][] trail = new double[SIZE][SIZE];
    private final Collection<Integer> bestRunVertexes = new CopyOnWriteArrayList<>();
    private final AntsStatistics statistics = new AntsStatistics();

    @Nonnegative
    private final int parallelAnts;

    public AntsColony(int parallelAnts) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        this.parallelAnts = parallelAnts;
    }

    @Override
    public long run(@Nonnegative long stopNanos) {
        log.debug("Colony start!");
        initialTrail();
        final long start = System.currentTimeMillis();
        runAnts(stopNanos);
        logResult(start);
        return statistics.getBestRunLength();
    }

    @Override
    @Nonnull
    public String getLog() {
        return statistics.getJournal();
    }

    private void initialTrail() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                trail[i][j] = INITIAL_TRAIL;
            }
        }
    }

    private void logResult(long startMilis) {
        log.info("Best tour: |{}" + '|' + "{} Total: {} ms", statistics.getBestRunLength(),
                OutputFormat.printIterableTour(bestRunVertexes),
                System.currentTimeMillis() - startMilis);
    }

    private void runAnts(long stopNanos) {
        final Runnable antRun = () -> {
            final AntRunResult runResult = new RunningAnt(GRAPH, trail.clone()).getRunResult();
            final String runJournal = runResult.getJournal();
            final long runLength = runResult.getLength();
            log.debug("ant run {} {}", runJournal, statistics.getBestRunLength());
            statistics.addFinishedRun(runLength);
            if (runResult.isSuccess()) {
                log.debug(runJournal);
                if (runLength < statistics.getBestRunLength()) {
                    final int[] currentRunTour = runResult.getTour();
                    bestRunVertexes.clear();
                    for (final int vertex : currentRunTour) {
                        bestRunVertexes.add(vertex);
                    }
                    statistics.setBestRunLength(runLength);
                    statistics.addGoodRun(runJournal);
                }
                applyPheromones(runResult);
            }

        };
        //noinspection MethodCallInLoopCondition - nonoTime need be called each time
        while (System.nanoTime() < stopNanos) {
            runExecutor(antRun, parallelAnts);
        }
    }

    private void applyPheromones(final AntRunResult runResult) {
        final double stick = 1.0D - EVAPORATION_COEFFICIENT;
        final int trailLength = trail.length;

        for (int i = 0; i < trailLength; i++) {
            for (int j = 0; j < i; j++) {
                double t = (stick * trail[i][j])
                        + runResult.getPheromonesDelta()[i][j]
                        + runResult.getPheromonesDelta()[j][i];
                trail[i][j] = t;
                trail[j][i] = t;
            }
        }
    }
}

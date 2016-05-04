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

package benchmarks.tasks.ants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;

import benchmarks.tasks.ants.ant.AntRunResult;
import benchmarks.tasks.ants.ant.RunningAnt;
import benchmarks.tasks.ants.data.IDistancesData;
import util.Restrictions;

import static benchmarks.tasks.ants.AntsColoniesSettings.EVAPORATION_COEFFICIENT;
import static benchmarks.tasks.ants.AntsColoniesSettings.INITIAL_TRAIL;
import static benchmarks.tasks.ants.AntsColoniesSettings.SOLUTION_EXCHANGE_NANOS;
import static benchmarks.tasks.ants.parallelisation.NParallelExecutor.runExecutor;

/**
 * Implements the Ants Colony optimization.
 *
 * @author Sergey Pomelov 20.01.15.
 * @see RunningAnt
 * @see AntsColoniesSettings
 */
public final class AntsColony implements IAntsOptimization {

    private static final long serialVersionUID = 1858529504894436119L;
    private static final Logger log = LoggerFactory.getLogger(AntsColony.class);

    @Nonnull
    private final String id;
    @Nonnull
    private final IDistancesData data;
    @Nonnull
    private final double[][] trail;
    @Nonnull
    private final Collection<Integer> bestRunVertexes = new CopyOnWriteArrayList<>();
    @Nonnull
    private final AntsStatistics statistics = new AntsStatistics();
    @Nonnegative
    private final int parallelAnts;
    @Nonnull
    private final AtomicBoolean gotNewSolutionToSend = new AtomicBoolean(true);
    @Nonnegative
    private final AtomicLong lastSendDataNanos = new AtomicLong(System.nanoTime());
    @GuardedBy("data")
    @Nonnull
    private List<IAntsOptimization> neighbours = Collections.emptyList();


    public AntsColony(String id, int parallelAnts, IDistancesData data) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        Restrictions.ifContainsNullFastFail(id, data);
        this.id = id;
        this.data = data;
        this.parallelAnts = parallelAnts;
        trail = new double[data.getSize()][data.getSize()];
    }

    @Override
    public long run(long periodNanos) {
        Restrictions.ifNotOnlyPositivesFastFail(periodNanos);
        log.debug("Colony {} start!", id);
        initialTrail();
        final long start = System.currentTimeMillis();
        runAnts(System.nanoTime() + periodNanos);
        logResult(start);
        return statistics.getBestRunLength();
    }

    @Override
    @Nonnull
    public String getLog() {
        return statistics.getJournal();
    }

    @Override
    public void addNeighbours(List<IAntsOptimization> neighbours) {
        if (neighbours != null) {
            synchronized (data) {
                this.neighbours = neighbours.stream().filter(colony -> colony != this)
                        .collect(Collectors.toList());
            }
        } else {
            log.warn("Neighbours must not be null!");
        }
    }

    @Override
    public void receiveSolution(AntRunResult antRunResult) {
        if (antRunResult != null) {
            applyPheromones(antRunResult);
            log.debug("Colony {} received a solution {}.", id, antRunResult.getLength());
        } else {
            log.warn("AntRunResult must not be null!");
        }
    }

    private void initialTrail() {
        for (int i = 0; i < data.getSize(); i++) {
            for (int j = 0; j < data.getSize(); j++) {
                trail[i][j] = INITIAL_TRAIL;
            }
        }
    }

    private void logResult(@Nonnegative long startMilis) {
        log.info("Colony {}, Best tour: |{}" + '|' + "{}. Total: {} ms", id,
                statistics.getBestRunLength(),
                OutputFormat.printIterableTour(bestRunVertexes),
                System.currentTimeMillis() - startMilis);
    }

    private void runAnts(@Nonnegative long stopNanos) {
        final Runnable antRun = () -> {
            final AntRunResult runResult = new RunningAnt(data, trail.clone()).getRunResult();
            final String runJournal = runResult.getJournal();
            final long runLength = runResult.getLength();
            //log.debug("Colony {}, ant run {} {}.", id, runJournal, statistics.getBestRunLength());
            statistics.addFinishedRun(runLength);
            if (runResult.isSuccess()) {
                if (runLength < statistics.getBestRunLength()) {
                    final int[] currentRunTour = runResult.getTour();
                    bestRunVertexes.clear();
                    for (final int vertex : currentRunTour) {
                        bestRunVertexes.add(vertex);
                    }
                    gotNewSolutionToSend.set(true);
                    statistics.setNewBestRun(runResult, runJournal);
                }
                applyPheromones(runResult);
            }

        };
        //noinspection MethodCallInLoopCondition - the nanoTime need be called each time
        while (System.nanoTime() < stopNanos) {
            runExecutor(antRun, parallelAnts, "colony" + id, "ant");
            sendSolutionIfNeed(System.nanoTime());
        }
    }

    private void sendSolutionIfNeed(long currentNanos) {
        if (gotNewSolutionToSend.get() &&
                (lastSendDataNanos.longValue() + SOLUTION_EXCHANGE_NANOS) >= currentNanos) {
            final AntRunResult bestRun = statistics.getBestRun();
            if (bestRun != null) {
                synchronized (data) {
                    neighbours.forEach(neighbour -> neighbour.receiveSolution(bestRun));
                }
                gotNewSolutionToSend.compareAndSet(true, false);
                lastSendDataNanos.set(System.nanoTime());
            }
        }
    }

    private void applyPheromones(@Nonnull AntRunResult runResult) {
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

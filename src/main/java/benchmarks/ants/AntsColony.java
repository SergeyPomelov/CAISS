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

package benchmarks.ants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import benchmarks.ants.ant.AntRunResult;
import benchmarks.ants.ant.RunningAnt;
import benchmarks.ants.data.IDistancesData;
import benchmarks.ants.parallelisation.ContinuousParallelExecutor;
import benchmarks.matrixes.metrics.PerformanceMeasurer;
import util.ConversionUtil;
import util.Restrictions;

/**
 * Implements the Ants Colony optimization.
 *
 * @author Sergey Pomelov 20.01.15.
 * @see RunningAnt
 * @see AntsSettings
 */
@ThreadSafe
public final class AntsColony implements IAntsColony {

    private static final long serialVersionUID = 1858529504894436119L;
    private static final Logger log = LoggerFactory.getLogger(AntsColony.class);

    @Nonnull
    private final String id;
    @Nonnull
    private final AntsSettings settings;
    @Nonnull
    private final PerformanceMeasurer colonyPerformanceMeasurer = new PerformanceMeasurer();
    @Nonnull
    private final float[][] trails;
    @Nonnull
    private final Collection<Integer> bestRunVertexes = new CopyOnWriteArrayList<>();
    @Nonnull
    private final AntsStatistics statistics = new AntsStatistics();
    @Nonnull
    private final List<PerformanceMeasurer> antsPerformanceMeasurers = new CopyOnWriteArrayList<>();
    @Nonnegative
    private final int parallelAnts;
    @Nonnull
    private final AtomicBoolean gotNewSolution = new AtomicBoolean(true);
    @Nonnegative
    private final AtomicLong lastSendDataNanos = new AtomicLong(System.nanoTime());
    @Nonnull
    private List<IAntsColony> neighbours = Collections.emptyList();

    AntsColony(String id, int parallelAnts, AntsSettings settings) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        Restrictions.ifContainsNullFastFail(id, settings);
        this.id = id;
        this.settings = settings;
        this.parallelAnts = parallelAnts;
        trails = new float[getData().getSize()][getData().getSize()];
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @Override
    public ColonyRunResult run(long periodNanos) {
        Restrictions.ifNotOnlyPositivesFastFail(periodNanos);
        log.debug("Colony {} start!", id);
        colonyPerformanceMeasurer.measurePerformance(this::initialTrail, "initialTrail");
        final long start = System.currentTimeMillis();
        runAnts(System.nanoTime() + periodNanos);
        logResult(start);
        return new ColonyRunResult(statistics.getBestRunLength(), neighbours.size() + 1,
                parallelAnts, colonyPerformanceMeasurer,
                PerformanceMeasurer.compileOverall(antsPerformanceMeasurers));
    }

    @Override
    @Nonnull
    public String getLog() {
        return statistics.getJournal();
    }

    @Override
    public void addNeighbours(List<IAntsColony> newNeighbours) {
        if (newNeighbours != null) {
            neighbours = ConversionUtil.nullFilter(newNeighbours).stream()
                    .filter(colony -> colony != this)
                    .collect(Collectors.toList());
        } else {
            log.warn("Neighbours must not be null!");
        }
    }

    @Override
    public void receiveSolution(AntRunResult antRunResult) {
        colonyPerformanceMeasurer.measurePerformance(() -> {
            if (antRunResult != null) {
                AntColonyInteraction
                        .takeActionsIfSolutionTheBest(antRunResult, statistics, bestRunVertexes,
                                gotNewSolution, true, settings, trails);
                log.debug("Colony {} received a solution {}.", id, antRunResult.getLength());
            } else {
                log.warn("Sent AntRunResult must not be null!");
            }
        }, "exchange");
    }

    private void initialTrail() {
        final int size = getData().getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trails[i][j] = getInitialTrail();
            }
        }
    }

    private void logResult(@Nonnegative long startMilis) {
        if (log.isInfoEnabled()) {
            log.info("Colony {}, Best tour: |{}" + '|' + "{}. Total: {} ms", id,
                    statistics.getBestRunLength(),
                    OutputFormat.printIterableTour(bestRunVertexes),
                    System.currentTimeMillis() - startMilis);
        }
    }

    private void runAnts(@Nonnegative long stopNanos) {
        final Callable<Long> antRun =
                AntColonyInteraction.interactionProcedure(id, settings, trails, statistics,
                        bestRunVertexes, gotNewSolution,
                        antsPerformanceMeasurers);
        //noinspection MethodCallInLoopCondition - the nanoTime need be called each time
        ContinuousParallelExecutor.run(antRun, parallelAnts,
                () -> System.nanoTime() >= stopNanos,
                this::sendSolutionIfNeed,
                "colony" + id,
                "ant");
    }

    private void sendSolutionIfNeed() {
        if (isTimeToSendSolution(System.nanoTime())) {
            final Optional<AntRunResult> bestRun = statistics.getBestRun();
            if (bestRun.isPresent()) {
                neighbours.forEach(neighbour -> neighbour.receiveSolution(bestRun.get()));
                gotNewSolution.compareAndSet(true, false);
                lastSendDataNanos.set(System.nanoTime());
            }
        }
    }

    private boolean isTimeToSendSolution(@Nonnegative long currentNanos) {
        return gotNewSolution.get() &&
                ((lastSendDataNanos.longValue() + getExchangeNanos()) < currentNanos);
    }

    @Nonnull
    private IDistancesData getData() {
        return settings.getGraph();
    }

    @Nonnegative
    private float getInitialTrail() {
        return settings.getInitialTrail();
    }

    @Nonnegative
    private long getExchangeNanos() {
        return settings.getExchangeNanos();
    }

}

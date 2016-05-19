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

package benchmarks.ants.colony;

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

import benchmarks.ants.AntsSettings;
import benchmarks.ants.colony.ant.AntRunResult;
import benchmarks.ants.colony.ant.RunningAnt;
import benchmarks.ants.data.IDistancesData;
import benchmarks.ants.parallelisation.ContinuousParallelExecutor;
import benchmarks.matrixes.metrics.PerformanceMeasurer;
import benchmarks.matrixes.metrics.PerformanceMeasuresCompiler;
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

    // immutable properties
    @Nonnull
    private final String id;
    @Nonnull
    private final AntsSettings settings;
    @Nonnegative
    private final int parallelAnts;

    // calculation process data
    @Nonnull
    private final ColonyCalculationData data;

    //  solutions exchanging
    @Nonnull
    private final AtomicBoolean gotNewSolution = new AtomicBoolean(true);
    @Nonnegative
    private final AtomicLong lastSendDataNanos = new AtomicLong(System.nanoTime());
    @Nonnull
    private List<IAntsColony> neighbours = Collections.emptyList();

    // performance data collection
    @Nonnull
    private final PerformanceMeasurer colonyPerformanceMeasurer = new PerformanceMeasurer();
    @Nonnull
    private final Collection<PerformanceMeasurer> antsPerformanceMeasurers = new CopyOnWriteArrayList<>();


    public AntsColony(String id, int parallelAnts, AntsSettings settings) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        Restrictions.ifContainsNullFastFail(id, settings);
        this.id = id;
        this.settings = settings;
        this.parallelAnts = parallelAnts;
        data = new ColonyCalculationData(settings);
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @Override
    public ColonyRunResult run(long periodNanos) {
        Restrictions.ifNotOnlyPositivesFastFail(periodNanos);
        log.debug("Colony {} start!", id);
        runAnts(System.nanoTime() + periodNanos);
        logResult();
        return new ColonyRunResult(id,
                getStatistics().getBestRunLength(), neighbours.size() + 1,
                parallelAnts, colonyPerformanceMeasurer,
                PerformanceMeasuresCompiler.compileOverall(antsPerformanceMeasurers));
    }

    @Override
    @Nonnull
    public String getLog() {
        return getStatistics().getJournal();
    }

    @Override
    public void addNeighbours(List<IAntsColony> neighboursToAdd) {
        if (neighboursToAdd != null) {
            //noinspection ObjectEquality, by design
            neighbours = ConversionUtil.nullFilter(neighboursToAdd).stream()
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
                AntColonyInteractions.takeActionsIfSolutionTheBest(this, antRunResult, true);
                log.debug("Colony {} received a solution {}.", id, antRunResult.getLength());
            } else {
                log.warn("Sent AntRunResult must not be null!");
            }
        }, "exchange");
    }

    void replaceBestRunVertexes(int... currentRunTour) {
        data.replaceBestRunVertexes(currentRunTour);
    }

    private void logResult() {
        if (log.isDebugEnabled()) {
            log.debug("Colony {}, Best tour: |{}" + '|' + "{}.", id,
                    getStatistics().getBestRunLength(),
                    OutputFormat.printIterableTour(data.getBestRunVertexes()));
        }
    }

    private void runAnts(@Nonnegative long stopNanos) {
        final Callable<Long> antRun =
                AntColonyInteractions.interactionProcedure(this);
        //noinspection MethodCallInLoopCondition - the nanoTime need be called each time
        ContinuousParallelExecutor.run(antRun, parallelAnts,
                () -> System.nanoTime() >= stopNanos,
                this::sendSolutionIfNeed, "colony" + id, "ant");
    }

    private void sendSolutionIfNeed() {
        if (isTimeToSendSolution(System.nanoTime())) {
            final Optional<AntRunResult> bestRun = getStatistics().getBestRun();
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
    String getId() {
        return id;
    }

    @Nonnull
    AntsSettings getSettings() {
        return settings;
    }

    @Nonnull
    float[][] getTrails() {
        return data.getTrails();
    }

    @Nonnull
    CachedRawEdgeQualities getQualities() {
        return data.getQualities();
    }

    @Nonnull
    AntsStatistics getStatistics() {
        return data.getStatistics();
    }

    @Nonnull
    Collection<Integer> getBestRunVertexes() {
        return data.getBestRunVertexes();
    }

    @Nonnull
    void gotNewSolution() {
        gotNewSolution.set(true);
    }

    @Nonnull
    IDistancesData getDistanceData() {
        return settings.getGraph();
    }

    @Nonnegative
    private long getExchangeNanos() {
        return settings.getExchangeNanos();
    }

    static String getRunJournal() {
        return ColonyCalculationData.getRunJournal();
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
        // accessible by design
    Collection<PerformanceMeasurer> getAntsPerformanceMeasurers() {
        return antsPerformanceMeasurers;
    }
}
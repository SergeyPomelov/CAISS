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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colony.ant.AntRunResult;
import benchmarks.ants.colony.ant.RunningAnt;
import benchmarks.ants.data.IDistancesData;
import benchmarks.ants.parallelisation.ContinuousParallelExecutor;
import benchmarks.metrics.PerformanceMeasurer;
import benchmarks.metrics.PerformanceMeasuresCompiler;
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
    @Nonnegative
    private final int parallelAnts;
    @Nonnull
    private final ColonyCalculationData data;
    @Nonnull
    private final SolutionsExchangeModule solutionsExchangeModule;
    @Nonnull
    private final PerformanceMeasurer colonyPerformanceMeasurer = new PerformanceMeasurer();
    @Nonnull
    private final Collection<PerformanceMeasurer> antsPerformanceMeasurers =
            new CopyOnWriteArrayList<>();

    public AntsColony(String id, int parallelAnts, AntsSettings settings,
                      CachedRawEdgeQualities qualities) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        Restrictions.ifContainsNullFastFail(id, settings);
        this.id = id;
        this.settings = settings;
        this.parallelAnts = parallelAnts;
        data = new ColonyCalculationData(settings, qualities);
        solutionsExchangeModule = new SolutionsExchangeModule();
    }

    @Override
    public ColonyRunResult run(long periodNanos) {
        Restrictions.ifNotOnlyPositivesFastFail(periodNanos);
        log.debug("Colony {} start!", id);
        runAnts(System.nanoTime() + periodNanos);
        logResult();
        return new ColonyRunResult(id,
                getStatistics().getBestRunLength(),
                solutionsExchangeModule.neighboursAmount() + 1,
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
            solutionsExchangeModule.setNeighbours(this, neighboursToAdd);
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
                AntColonyInteractions.antRunProcedure(this);
        //noinspection MethodCallInLoopCondition - the nanoTime need be called each time
        ContinuousParallelExecutor.run(antRun, parallelAnts,
                () -> System.nanoTime() >= stopNanos,
                this::sendSolutionIfNeed, "colony" + id, "ant");
    }

    private void sendSolutionIfNeed() {
        solutionsExchangeModule.sendSolutionsIfNeed(getStatistics(), settings);
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
    void gotNewSolution() {
        solutionsExchangeModule.gotNewSolution();
    }

    @Nonnull
    IDistancesData getDistanceData() {
        return settings.getGraph();
    }

    @SuppressWarnings("unused")
    @Nonnegative
    private long getExchangeNanos() {
        return settings.getExchangeNanos();
    }

    // accessible by design
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    Collection<PerformanceMeasurer> getAntsPerformanceMeasurers() {
        return antsPerformanceMeasurers;
    }
}

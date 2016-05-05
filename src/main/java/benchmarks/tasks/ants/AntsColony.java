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
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import benchmarks.tasks.ants.ant.AntRunResult;
import benchmarks.tasks.ants.ant.RunningAnt;
import benchmarks.tasks.ants.data.IDistancesData;
import benchmarks.tasks.ants.parallelisation.ContinuousParallelExecutor;
import util.ConversionUtil;
import util.Restrictions;

import static benchmarks.tasks.ants.AntColonyInteraction.interactionProcedure;
import static benchmarks.tasks.ants.AntColonyInteraction.takeActionsIfSolutionTheBest;
import static benchmarks.tasks.ants.AntsSettings.INITIAL_TRAIL;
import static benchmarks.tasks.ants.AntsSettings.SOLUTION_EXCHANGE_NANOS;

/**
 * Implements the Ants Colony optimization.
 *
 * @author Sergey Pomelov 20.01.15.
 * @see RunningAnt
 * @see AntsSettings
 */
final class AntsColony implements IAntsColony {

    private static final long serialVersionUID = 1858529504894436119L;
    private static final Logger log = LoggerFactory.getLogger(AntsColony.class);

    @Nonnull
    private final String id;
    @Nonnull
    private final IDistancesData data;
    @Nonnull
    private final float[][] trails;
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
    @Nonnull
    private List<IAntsColony> neighbours = Collections.emptyList();

    AntsColony(String id, int parallelAnts, IDistancesData data) {
        Restrictions.ifNotOnlyPositivesFastFail(parallelAnts);
        Restrictions.ifContainsNullFastFail(id, data);
        this.id = id;
        this.data = data;
        this.parallelAnts = parallelAnts;
        trails = new float[data.getSize()][data.getSize()];
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
    public void addNeighbours(List<IAntsColony> neighbours) {
        if (neighbours != null) {
            synchronized (data) {
                this.neighbours = ConversionUtil.nullFilter(neighbours).stream()
                        .filter(colony -> colony != this)
                        .collect(Collectors.toList());
            }
        } else {
            log.warn("Neighbours must not be null!");
        }
    }

    @Override
    public void receiveSolution(AntRunResult antRunResult) {
        if (antRunResult != null) {
            takeActionsIfSolutionTheBest(antRunResult, statistics, bestRunVertexes,
                    gotNewSolutionToSend, true, trails);
            log.debug("Colony {} received a solution {}.", id, antRunResult.getLength());
        } else {
            log.warn("Sent AntRunResult must not be null!");
        }
    }

    private void initialTrail() {
        final int size = data.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trails[i][j] = INITIAL_TRAIL;
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
        final Callable<Long> antRun = interactionProcedure(data, trails, statistics,
                bestRunVertexes, gotNewSolutionToSend);
        //noinspection MethodCallInLoopCondition - the nanoTime need be called each time
        ContinuousParallelExecutor.run(antRun, parallelAnts,
                () -> System.nanoTime() >= stopNanos,
                () -> sendSolutionIfNeed(System.nanoTime()),
                "colony" + id,
                "ant");
    }

    private void sendSolutionIfNeed(@Nonnegative long currentNanos) {
        if (isTimeToSendSolution(currentNanos)) {
            final AntRunResult bestRun = statistics.getBestRun();
            if (bestRun != null) {
                neighbours.forEach(neighbour -> neighbour.receiveSolution(bestRun));
                gotNewSolutionToSend.compareAndSet(true, false);
                lastSendDataNanos.set(System.nanoTime());
            }
        }
    }

    private boolean isTimeToSendSolution(@Nonnegative long currentNanos) {
        return gotNewSolutionToSend.get() &&
                ((lastSendDataNanos.longValue() + SOLUTION_EXCHANGE_NANOS) >= currentNanos);
    }
}

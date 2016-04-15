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

import com.google.common.util.concurrent.AtomicDouble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static util.Constants.LS;

@Immutable
public final class AntsColony implements IAntsOptimization {

    private static final long serialVersionUID = 1858529504894436119L;
    private static final Logger log = LoggerFactory.getLogger(AntsColony.class);
    private static final IGraph GRAPH = new GraphMatrix();
    private static final int SIZE = GRAPH.getSize();
    private static final double EVAPORATION_COEFFICIENT = 0.1;
    private static final int ANTS_COUNT = 32;
    private static final double VAL = 1;

    private final StringBuilder out = new StringBuilder(512);
    private final double[][] trail = new double[SIZE][SIZE];
    private final Collection<Integer> bestRunVertexes = new CopyOnWriteArrayList<>();

    private final AtomicBoolean mutex = new AtomicBoolean();
    private final AtomicInteger antsGoodRuns = new AtomicInteger(0);
    private final AtomicInteger bestRunLength = new AtomicInteger(Integer.MAX_VALUE);
    private final AtomicDouble avg = new AtomicDouble(VAL);

    private AntsColony() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                trail[i][j] = VAL;
            }
        }

        runAnts();

        log.info("Ant optimization finished!");
        final long start = System.currentTimeMillis();
        out.append("Best: |").append(bestRunLength).append('|')
                .append(printArray(bestRunVertexes)).append(" Total: ")
                .append(System.currentTimeMillis() - start).append(" ms");
    }

    @Nonnull
    public static IAntsOptimization getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    @Nonnull
    public String getLog() {
        return out.toString();
    }

    @Nonnull
    private static String printArray(final Iterable<Integer> list) {
        final StringBuilder out = new StringBuilder(64);
        for (final int anArray : list) {
            out.append(anArray + 1).append('>');
        }
        return out.toString();
    }

    private void runAnts() {
        final ThreadPoolExecutor executor =
                new ThreadPoolExecutor(1, 100, 1, TimeUnit.SECONDS,
                        new SynchronousQueue<>(), runnable -> {
                    final Thread thread = new Thread(runnable, "ant");
                    thread.setDaemon(true);
                    thread.setPriority(Thread.NORM_PRIORITY);
                    return thread;
                });

        final Runnable antRun = () -> {
            final AntRunResult runResult = new RunningAnt(GRAPH, trail.clone(), avg.get())
                    .getRunResult();
            out.append(runResult.getJournal()).append(LS);
            log.info("ant run {} {}", runResult.getJournal(), bestRunLength);

            if (runResult.getLen() < bestRunLength.intValue()) {
                final int[] currentRunTour = runResult.getTour();
                bestRunLength.set((int) runResult.getLen());
                bestRunVertexes.clear();
                for (final int vertex : currentRunTour) {
                    bestRunVertexes.add(vertex);
                }

                final double avgDivider = 0.5 * StrictMath.pow(runResult.getTour().length, 2);
                avg.set(avg.get() / avgDivider);
                log.info("ant find better: {} {}",
                        antsGoodRuns.incrementAndGet(),
                        runResult.getJournal());
            }
            applyPheromones(runResult);
        };

        out.append(LS).append("====================AntsRun=========================").append(LS);
        for (int antsRuns = 0; antsRuns < ANTS_COUNT; antsRuns++) {
            executor.submit(antRun);
        }

        //noinspection MethodCallInLoopCondition, by design
        while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        executor.shutdown();
    }

    private void applyPheromones(final AntRunResult runResult) {
        final double min = avg.get() / runResult.getTour().length;
        double max = 0;
        final double stick = 1 - EVAPORATION_COEFFICIENT;
        avg.set(0);

        final int trailLength = trail.length;
        for (int i = 0; i < trailLength; i++) {
            for (int j = 0; j < i; j++) {
                double t = (stick * trail[i][j]) + (EVAPORATION_COEFFICIENT *
                        (runResult.getDelta()[i][j] + runResult.getDelta()[j][i]));
                if (t < min) {
                    t = min;
                }
                trail[i][j] = t;
                trail[j][i] = t;
                if (t > max) {
                    max = t;
                }
                synchronized (mutex) {
                    avg.set(avg.get() + t);
                }
            }
        }
    }

    private static final class SingletonHolder {
        @Nonnull
        private static final IAntsOptimization INSTANCE = new AntsColony();
    }
}

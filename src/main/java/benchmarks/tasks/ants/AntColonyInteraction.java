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

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.tasks.ants.ant.AntRunResult;
import benchmarks.tasks.ants.ant.RunningAnt;
import benchmarks.tasks.ants.data.IDistancesData;

import static benchmarks.tasks.ants.PheromonesApplier.applyPheromones;

/**
 * @author Sergey Pomelov on 04/05/2016.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
@ParametersAreNonnullByDefault
final class AntColonyInteraction {

    private AntColonyInteraction() { /* package local utility class*/ }

    static Callable<Long> interactionProcedure(
            IDistancesData data, float[][] trails, AntsStatistics statistics,
            Collection<Integer> bestRunVertexes, AtomicBoolean gotNewSolution) {

        return () -> {
            final AntRunResult runResult = new RunningAnt(data, trails).getRunResult();
            final String runJournal = runResult.getJournal();
            final long runLength = runResult.getLength();
            //log.debug("Colony {}, ant run {} {}.", id, runJournal, statistics.getBestRunLength());
            statistics.addFinishedRun(runLength);
            takeActionsIfSolutionTheBest(runResult, runLength, statistics, bestRunVertexes,
                    gotNewSolution, false, runJournal, trails);
            return runLength;
        };
    }

    static void takeActionsIfSolutionTheBest(AntRunResult runResult,
                                             AntsStatistics statistics,
                                             Collection<Integer> bestRunVertexes,
                                             AtomicBoolean gotNewSolution,
                                             boolean gotOutside,
                                             float[][] trails) {
        takeActionsIfSolutionTheBest(runResult, runResult.getLength(), statistics, bestRunVertexes,
                gotNewSolution, gotOutside, runResult.getJournal(), trails);
    }

    private static void takeActionsIfSolutionTheBest(AntRunResult runResult,
                                                     long runLength,
                                                     AntsStatistics statistics,
                                                     Collection<Integer> bestRunVertexes,
                                                     AtomicBoolean gotNewSolution,
                                                     boolean gotOutside,
                                                     String runJournal,
                                                     float[][] trails) {
        if (runResult.isSuccess()) {
            if (runLength < statistics.getBestRunLength()) {
                changeTheBestSolution(runResult, bestRunVertexes, gotNewSolution,
                        statistics, gotOutside, runJournal);
            }
            applyPheromones(runResult, trails);
        }
    }

    private static void changeTheBestSolution(AntRunResult runResult,
                                              Collection<Integer> bestRunVertexes,
                                              AtomicBoolean gotNewSolution,
                                              AntsStatistics statistics,
                                              boolean gotOutside,
                                              String runJournal) {

        final int[] currentRunTour = runResult.getTour();
        bestRunVertexes.clear();
        for (final int vertex : currentRunTour) {
            bestRunVertexes.add(vertex);
        }
        if (!gotOutside) {
            gotNewSolution.set(true);
        }
        statistics.setNewBestRun(runResult, runJournal);
    }

}

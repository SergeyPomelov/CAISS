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

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.ants.colony.ant.AntRunResult;
import benchmarks.ants.colony.ant.RunningAnt;

/**
 * @author Sergey Pomelov on 04/05/2016.
 */
// this is an utility AntColony methods delegate
@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "FeatureEnvy"})
@ParametersAreNonnullByDefault
final class AntColonyInteractions {

    private static final Logger log = LoggerFactory.getLogger(AntColonyInteractions.class);

    private AntColonyInteractions() { /* package local utility class*/ }

    static Callable<Long> interactionProcedure(AntsColony antsColony) {
        return () -> {
            final Optional<AntRunResult> runResult = new RunningAnt(antsColony.getDistanceData(),
                    antsColony.getQualities(), antsColony.getTrails()).getRunResult();

            if (runResult.isPresent()) {
                return processResult(runResult.get(), antsColony);
            }
            log.warn("No ant's result obtained!");
            return Long.MAX_VALUE;
        };
    }

    private static long processResult(AntRunResult runResult, AntsColony antsColony) {
        final String runJournal = runResult.getJournal();
        final long runLength = runResult.getLength();
        final AntsStatistics statistics = antsColony.getStatistics();
        log.debug("Colony {}, ant run {} {}.",
                antsColony.getId(), runJournal, statistics.getBestRunLength());

        statistics.addFinishedRun(runLength);
        antsColony.getAntsPerformanceMeasurers().add(runResult.getPerformanceMeasurer());
        takeActionsIfSolutionTheBest(antsColony, runResult, false);
        return runLength;
    }

    static void takeActionsIfSolutionTheBest(AntsColony antsColony,
                                             AntRunResult runResult,
                                             boolean gotOutside) {
        if (runResult.isSuccess()) {
            if (runResult.getLength() < antsColony.getStatistics().getBestRunLength()) {
                changeTheBestSolution(antsColony, runResult, gotOutside);
            }
            PheromonesApplier.applyPheromones(runResult, antsColony.getSettings(),
                    antsColony.getTrails());
        }
    }

    private static void changeTheBestSolution(AntsColony antsColony,
                                              AntRunResult runResult,
                                              boolean gotOutside) {

        antsColony.replaceBestRunVertexes(runResult.getTour());
        if (!gotOutside) {
            antsColony.gotNewSolution();
        }
        antsColony.getStatistics().setNewBestRun(runResult, AntsColony.getRunJournal());
    }

}

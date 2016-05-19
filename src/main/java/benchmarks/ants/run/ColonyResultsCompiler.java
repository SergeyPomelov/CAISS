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

package benchmarks.ants.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import benchmarks.ants.colony.ColonyRunResult;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
public final class ColonyResultsCompiler {

    private static final Logger log = LoggerFactory.getLogger(ColonyResultsCompiler.class);

    private ColonyResultsCompiler() { /* utility class */ }

    @SuppressWarnings({"NumericCastThatLosesPrecision", "FeatureEnvy"})
    public static ColonyRunResult compileOverallResults(Collection<ColonyRunResult>
                                                                colonyRunResults,
                                                        int colonies, int ants) {
        final StringBuilder id = new StringBuilder("");
        long result = Long.MAX_VALUE;
        double antRuns = 0L;
        double exchanges = 0L;
        double avgInitialTrailNs = 0L;
        double avgAntsRunNs = 0L;
        double avgExchangeNs = 0L;

        final int size = colonyRunResults.size();
        if (size > 0) {
            int i = 1;
            for (ColonyRunResult runResult : colonyRunResults) {
                if (isCompatibleTask(colonies, ants, runResult)) {
                    id.append(runResult.getId()).append(';');
                    if (runResult.getResult() < result) {
                        result = runResult.getResult();
                    }
                    antRuns += runResult.getAntRuns();
                    exchanges += runResult.getExchanges();
                    avgInitialTrailNs = avg(avgInitialTrailNs, runResult.getAvgInitialTrailNs(), i);
                    avgAntsRunNs = avg(avgAntsRunNs, runResult.getAvgAntsRunNs(), i);
                    avgExchangeNs = avg(avgExchangeNs, runResult.getAvgExchangeNs(), i);
                    i++;
                }
            }
        }

        return new ColonyRunResult(id.toString(), result, colonies, ants, (long) antRuns, (long)
                exchanges, (long) avgInitialTrailNs, (long) avgAntsRunNs, (long) avgExchangeNs);
    }

    @SuppressWarnings("FeatureEnvy")
    private static boolean isCompatibleTask(int colonies, int ants, ColonyRunResult runResult) {
        if (!runResult.isValid(colonies, ants)) {
            log.warn("Averaging not matching or valid task, ignored. Colonies: {}. Ants: {}. " +
                    "Task to average {}.", colonies, ants, runResult);
            return false;
        }
        return true;
    }

    private static double avg(double avgOld, double newValue, double alreadyValuesAdded) {
        return ((avgOld * (alreadyValuesAdded - 1)) + newValue) / alreadyValuesAdded;
    }
}

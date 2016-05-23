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

package benchmarks.ants.colonies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.ants.colonies.colony.AntsColony;
import benchmarks.ants.colonies.colony.ColonyRunResult;
import benchmarks.ants.colonies.colony.IAntsColony;
import benchmarks.ants.colonies.parallelisation.ParallelBarrierExecutor;
import util.Restrictions;

import static benchmarks.ants.colonies.ColonyResultsCompiler.compileOverallResult;


/**
 * @author Sergey Pomelov on 02/05/2016.
 */
@SuppressWarnings("FeatureEnvy")
@ParametersAreNonnullByDefault
public final class AntsColonies {

    private AntsColonies() { /* utility class */ }

    public static ColonyRunResult runCalculations(AntsExperimentData data) {
        Restrictions.ifContainsNullFastFail(data.getSettings());
        Restrictions.ifNotOnlyPositivesFastFail(data.getColonies(), data.getAnts());
        return compileOverallResult(generateSolutions(data),
                data.getColonies(), data.getAnts());
    }

    private static Collection<ColonyRunResult> generateSolutions(AntsExperimentData data) {
        final List<ColonyRunResult> solutions = new ArrayList<>(data.getColonies());
        ParallelBarrierExecutor.runOnce(
                generateAgents(data).stream()
                        .map(colony -> (Runnable) () ->
                                solutions.add(colony.run(data.getSettings().getRunPeriodNanos())))
                        .collect(Collectors.toList()), "start", "colony");
        return solutions;
    }

    private static Collection<IAntsColony> generateAgents(AntsExperimentData data) {
        final int coloniesAmount = data.getColonies();
        final List<IAntsColony> colonies = new ArrayList<>(coloniesAmount);
        for (int i = 0; i < coloniesAmount; i++) {
            //noinspection ObjectAllocationInLoop, by design
            final IAntsColony colony = new AntsColony(String.valueOf(i + 1), data.getAnts(),
                    data.getSettings(), data.getQualities());
            colonies.add(colony);
        }
        colonies.forEach(colony -> colony.addNeighbours(colonies));
        return colonies;
    }
}

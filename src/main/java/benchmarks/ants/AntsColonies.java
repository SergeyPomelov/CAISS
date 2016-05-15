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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;

import benchmarks.ants.parallelisation.ParallelExecutor;
import util.Restrictions;

import static benchmarks.ants.ColonyRunResult.compileOverallResults;

/**
 * @author Sergey Pomelov on 02/05/2016.
 */
@ParametersAreNonnullByDefault
final class AntsColonies {

    private static final Logger log = LoggerFactory.getLogger(AntsColonies.class);

    private AntsColonies() { /* utility class */ }

    static ColonyRunResult runCalculations(int coloniesAmount, int antsPerColony,
                                           AntsSettings settings) {
        Restrictions.ifContainsNullFastFail(settings);
        Restrictions.ifNotOnlyPositivesFastFail(coloniesAmount, antsPerColony);
        return compileOverallResults(generateSolutions(coloniesAmount, antsPerColony, settings),
                coloniesAmount, antsPerColony);
    }

    private static List<ColonyRunResult> generateSolutions(@Nonnegative int coloniesAmount,
                                                           @Nonnegative int antsPerColony,
                                                           AntsSettings settings) {
        final List<ColonyRunResult> solutions = new ArrayList<>(coloniesAmount);
        ParallelExecutor.runOnce(
                generateAgents(coloniesAmount, antsPerColony, settings).stream()
                        .map(colony -> (Runnable) () ->
                                solutions.add(colony.run(settings.getRunPeriodNanos())))
                        .collect(Collectors.toList()),
                "start", "colony");

        return solutions;
    }

    private static Collection<IAntsColony> generateAgents(@Nonnegative int coloniesAmount,
                                                          @Nonnegative int antsPerColony,
                                                          AntsSettings settings) {
        final List<IAntsColony> colonies = new ArrayList<>(coloniesAmount);
        for (int i = 0; i < coloniesAmount; i++) {
            //noinspection ObjectAllocationInLoop, by design
            final IAntsColony colony = new AntsColony(String.valueOf(i + 1), antsPerColony,
                    settings);
            colonies.add(colony);
        }
        colonies.forEach(colony -> colony.addNeighbours(colonies));
        return colonies;
    }
}

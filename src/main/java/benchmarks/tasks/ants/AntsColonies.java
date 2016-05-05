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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import benchmarks.tasks.ants.data.IDistancesData;
import benchmarks.tasks.ants.parallelisation.ParallelExecutor;
import util.Restrictions;

import static benchmarks.tasks.ants.AntsSettings.RUN_PERIOD_NANOS;

/**
 * @author Sergey Pomelov on 02/05/2016.
 */
public final class AntsColonies {

    private static final Logger log = LoggerFactory.getLogger(AntsColonies.class);

    private AntsColonies() { /* utility class */ }

    public static Long runCalculations(int coloniesAmount, int antsPerColony, IDistancesData data) {
        Restrictions.ifContainsNullFastFail(data);
        Restrictions.ifNotOnlyPositivesFastFail(coloniesAmount, antsPerColony);
        return generateSolutions(coloniesAmount, data).first();
    }

    private static SortedSet<Long> generateSolutions(@Nonnegative int coloniesAmount,
                                                     @Nonnull IDistancesData data) {
        final SortedSet<Long> solutions = new TreeSet<>();
        ParallelExecutor.runOnce(
                generateAgents(coloniesAmount, data).stream()
                        .map(colony -> (Runnable) () -> solutions.add(colony.run(RUN_PERIOD_NANOS)))
                        .collect(Collectors.toList()),
                "start", "colony");
        if (log.isDebugEnabled()) {
            log.debug("Solutions: {}, the best {}.", solutions, solutions.first());
        }
        return solutions;
    }

    private static Collection<IAntsColony> generateAgents(@Nonnegative int coloniesAmount,
                                                          @Nonnull IDistancesData data) {
        final List<IAntsColony> colonies = new ArrayList<>(coloniesAmount);
        for (int i = 0; i < coloniesAmount; i++) {
            final IAntsColony colony = new AntsColony(String.valueOf(i + 1), coloniesAmount,
                    data);
            colonies.add(colony);
        }
        colonies.forEach(colony -> colony.addNeighbours(colonies));
        return colonies;
    }
}

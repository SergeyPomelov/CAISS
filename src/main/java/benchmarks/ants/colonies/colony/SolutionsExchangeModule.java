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

package benchmarks.ants.colonies.colony;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colonies.colony.ant.AntRunResult;
import util.ConversionUtil;

/**
 * @author Sergey Pomelov on 23/05/2016.
 */
@ThreadSafe
@ParametersAreNonnullByDefault
final class SolutionsExchangeModule implements Serializable {

    private static final long serialVersionUID = 5661347083565948491L;

    @Nonnull
    private final AtomicBoolean gotNewSolution = new AtomicBoolean(true);
    @Nonnegative
    private final AtomicLong lastSendDataNanos = new AtomicLong(System.nanoTime());
    @Nonnull
    private List<IAntsColony> neighbours = Collections.emptyList();

    SolutionsExchangeModule() {
    }

    void sendSolutionsIfNeed(AntsStatistics statistics, AntsSettings settings) {
        if (isTimeToSendSolution(settings)) {
            final Optional<AntRunResult> bestRun = statistics.getBestRun();
            if (bestRun.isPresent()) {
                sendSolutions(bestRun.get());
            }
        }
    }

    private boolean isTimeToSendSolution(AntsSettings settings) {
        return gotNewSolution.get() &&
                ((lastSendDataNanos.longValue() + settings.getExchangeNanos()) <
                        System.nanoTime());
    }

    private void sendSolutions(AntRunResult antRunResult) {
        neighbours.forEach(neighbour -> neighbour.receiveSolution(antRunResult));
        gotNewSolution.compareAndSet(true, false);
        lastSendDataNanos.set(System.nanoTime());
    }

    void gotNewSolution() {
        gotNewSolution.set(true);
    }

    void setNeighbours(@Nonnull IAntsColony owner, Collection<IAntsColony> neighboursToAdd) {
        neighbours = ConversionUtil.nullFilter(neighboursToAdd).stream()
                .filter(colony -> !Objects.equals(colony, owner))
                .collect(Collectors.toList());
    }

    int neighboursAmount() {
        return neighbours.size();
    }
}

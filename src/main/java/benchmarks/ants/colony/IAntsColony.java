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

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import benchmarks.ants.colony.ant.AntRunResult;

/**
 * The interface for Ant's optimization.
 *
 * @author Sergey Pomelov 20.01.15.
 */
public interface IAntsColony extends Serializable {
    /**
     * @param periodNanos - The time stop criteria.
     * @return the best solution length
     */
    @Nonnegative
    ColonyRunResult run(@Nonnegative long periodNanos);

    /**
     * @param neighbours - Colonies to send them the best solution. The "time to time" defined
     *                   inside the
     *                   {@code benchmarks.tasks.ants.AntsColoniesSettings#EXCHANGE_NANOS}.
     */
    void addNeighbours(List<IAntsColony> neibhoursToAdd);

    /**
     * @param antRunResult - method implementing obtaining the information about other colony's best
     *                     solution.
     */
    void receiveSolution(AntRunResult antRunResult);

    /**
     * @return the additional computation logs
     */
    @Nonnull
    String getLog();
}

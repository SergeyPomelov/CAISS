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

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * The interface for Ant's optimization.
 *
 * @author Sergey Pomelov 20.01.15.
 */
public interface IAntsOptimization extends Serializable {
    /**
     * @param stopNanos - The time stop criteria.
     * @return the best solution length
     */
    @Nonnegative
    long run(@Nonnegative long stopNanos);

    /**
     * @return the additional computation logs
     */
    @Nonnull
    String getLog();
}

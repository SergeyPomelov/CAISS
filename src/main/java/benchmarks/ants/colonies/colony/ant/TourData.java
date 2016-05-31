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

package benchmarks.ants.colonies.colony.ant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * Class for saving the tour results.
 * @author Sergey Pomelov on 29/04/2016.
 * @see AntRunResult
 */
@Immutable
@ParametersAreNonnullByDefault
final class TourData {
    private final boolean success;
    @Nonnull
    private final int[] tour;
    @Nonnegative
    private final long length;

    TourData(boolean success, int[] tour, @Nonnegative long length) {
        this.success = success;
        this.tour = tour;
        this.length = length;
    }

    boolean isSuccess() {
        return success;
    }

    // memory & performance issue, additional guaranties omitted.
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    @Nonnull
    int[] getTour() {
        return tour;
    }

    @Nonnegative
    long getLength() {
        return length;
    }
}

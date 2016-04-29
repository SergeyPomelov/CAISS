/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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

package benchmarks.tasks.ants.ant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * @author Sergey Pomelov on 29/04/2016. Class for saving the tour results.
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
        this.tour = tour.clone();
        this.length = length;
    }

    public boolean isSuccess() {
        return success;
    }

    @Nonnull
    public int[] getTour() {
        return tour.clone();
    }

    @Nonnegative
    public long getLength() {
        return length;
    }
}

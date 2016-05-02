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

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * @author Sergey Pomelov on 28/04/2016.
 */
public final class OutputFormat {

    private OutputFormat() { /* utility class */ }

    @Nonnull
    public static String printTour(int... iterable) {
        return printIterableTour(Arrays.stream(iterable).boxed().collect(Collectors.toList()));
    }

    @Nonnull
    static String printIterableTour(final Iterable<Integer> iterable) {
        final StringBuilder out = new StringBuilder(64);
        for (final int element : iterable) {
            out.append(element + 1).append('>');
        }
        return out.toString();
    }
}

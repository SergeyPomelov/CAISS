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

package benchmarks.ants.colony.ant;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

/**
 * @author Sergey Pomelov on 15/05/2016. Class represents float[][] array deltas for memory saving
 *         purposes.
 */
@SuppressWarnings("WeakerAccess") // false-positive claim, it used outside
@Immutable
public final class Array2DFloatDelta {

    private final Map<Integer, Map<Integer, Float>> deltas;

    private Array2DFloatDelta(Map<Integer, Map<Integer, Float>> deltas) {
        this.deltas = deltas;
    }

    public float get(int i, int j) {
        final Map<Integer, Float> deltasJ = deltas.get(i);
        return (deltasJ != null) ? deltasJ.getOrDefault(j, 0.0F) : 0.0F;
    }

    @SuppressWarnings("PackageVisibleInnerClass") // builder pattern is ok
    static class Builder {
        private Map<Integer, Map<Integer, Float>> temporaryDeltas = new HashMap<>(0);

        Builder(int size) {
            temporaryDeltas = new HashMap<>(size, 0.5F);
        }

        @SuppressWarnings("UnusedReturnValue") // builder pattern still is ok
        public Builder add(int row, int column, float delta) {
            final Map<Integer, Float> rowEntry = temporaryDeltas.get(row);
            if (rowEntry != null) {
                rowEntry.put(column, delta);
            } else {
                final Map<Integer, Float> newRowEntry = new HashMap<>();
                newRowEntry.put(column, delta);
                temporaryDeltas.put(row, newRowEntry);
            }
            return this;
        }

        Array2DFloatDelta build() {
            return new Array2DFloatDelta(temporaryDeltas);
        }
    }
}

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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Class represents Hamiltonian path through 2D array graph form's
 * ant trail deltas in memory saving format.
 * @author Sergey Pomelov on 15/05/2016.
 */
@SuppressWarnings("WeakerAccess") // false-positive claim, it used outside
@Immutable
public final class TourTrailDelta {

    private final float[] deltas;
    private final int[] columnIdx;

    @SuppressWarnings("MethodCanBeVariableArityMethod")
    public TourTrailDelta(float[] deltas, int[] columnIdx) {
        this.deltas = deltas;
        this.columnIdx = columnIdx;
    }

    @Nonnull
    public float getDelta(int row) {
        return deltas[row];
    }

    public int getDeltaColumnIdx(int row) {
        return columnIdx[row];
    }

    @SuppressWarnings("PackageVisibleInnerClass") // inner builder pattern is ok
    static class Builder {

        private final float[] deltas;
        private final int[] columnIdx;

        Builder(int size) {
            deltas = new float[size];
            columnIdx = new int[size];
        }

        @SuppressWarnings("UnusedReturnValue") // builder pattern still is ok
        public Builder add(int row, int column, float delta) {
            deltas[row] = delta;
            columnIdx[row] = column;
            return this;
        }

        TourTrailDelta build() {
            return new TourTrailDelta(deltas, columnIdx);
        }
    }
}

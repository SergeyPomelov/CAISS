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

import javax.annotation.Nonnull;

import benchmarks.ants.data.IDistancesData;

/**
 * Precalculated (in assumption that the graph while ants colony runs immutable)
 * part of the trail equation. Performance issue.
 * @author Sergey Pomelov on 11/05/2016.
 */
public final class CachedRawEdgeQualities implements Serializable {

    private static final long serialVersionUID = 3487732853804434640L;
    @Nonnull
    private final float[][] edgesQualities;

    public CachedRawEdgeQualities(IDistancesData data) {
        final int size = data.getSize();
        edgesQualities = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //noinspection NumericCastThatLosesPrecision, used like pow(float, float)
                edgesQualities[i][j] = (float) StrictMath.pow(data.getDist(i, j), -1.0D);
            }
        }
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField") // performance issue
    @Nonnull
    public float[][] getEdgesQualities() {
        return edgesQualities;
    }
}

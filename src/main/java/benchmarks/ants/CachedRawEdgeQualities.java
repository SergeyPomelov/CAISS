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

package benchmarks.ants;

import javax.annotation.Nonnull;

import benchmarks.ants.data.IDistancesData;

/**
 * @author Sergey Pomelov on 11/05/2016.
 */
@SuppressWarnings({"StaticNonFinalField", "FieldRepeatedlyAccessedInMethod"})
// this double-checked singleton pattern is ok
public final class CachedRawEdgeQualities {

    private static volatile CachedRawEdgeQualities instance = null;
    @Nonnull
    private final float[][] edgesQualities;

    private CachedRawEdgeQualities(IDistancesData data) {
        final int size = data.getSize();
        edgesQualities = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //noinspection NumericCastThatLosesPrecision, used like pow(float, float)
                edgesQualities[i][j] = (float) StrictMath.pow(data.getDist(i, j), -1.0D);
            }
        }
    }

    public static CachedRawEdgeQualities getInstance(IDistancesData data) {
        CachedRawEdgeQualities localInstance = instance;
        if (localInstance == null) {
            synchronized (CachedRawEdgeQualities.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CachedRawEdgeQualities(data);
                }
            }
        }
        return localInstance;
    }

    public float getDist(int i, int j) {
        return edgesQualities[i][j];
    }
}

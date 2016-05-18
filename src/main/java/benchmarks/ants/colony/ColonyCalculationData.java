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

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import benchmarks.ants.AntsSettings;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
@ThreadSafe
final class ColonyCalculationData implements Serializable {

    private static final long serialVersionUID = -1491221017205795015L;

    @Nonnull
    private final float[][] trails;
    @Nonnull
    private final CachedRawEdgeQualities qualities;
    @Nonnull
    private final Collection<Integer> bestRunVertexes = new CopyOnWriteArrayList<>();
    @Nonnull
    private final AntsStatistics statistics = new AntsStatistics();

    ColonyCalculationData(AntsSettings settings) {
        final int size = settings.getGraph().getSize();
        qualities = new CachedRawEdgeQualities(settings.getGraph());
        trails = new float[size][size];
        initialTrail(size, settings);
    }

    void replaceBestRunVertexes(int[] currentRunTour) {
        bestRunVertexes.clear();
        bestRunVertexes.addAll(Arrays.stream(currentRunTour).boxed().collect(Collectors.toList()));
    }

    private void initialTrail(int size, AntsSettings settings) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trails[i][j] = settings.getInitialTrail();
            }
        }
    }

    // coping or immutable wrapping affects performance
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    @Nonnull
    float[][] getTrails() {
        return trails;
    }

    @Nonnull
    CachedRawEdgeQualities getQualities() {
        return qualities;
    }

    @Nonnull
    Collection<Integer> getBestRunVertexes() {
        return ImmutableList.copyOf(bestRunVertexes);
    }

    @Nonnull
    AntsStatistics getStatistics() {
        return statistics;
    }

    @SuppressWarnings("MethodReturnAlwaysConstant") // stub
    @Nonnull
    static String getRunJournal() {
        return "";
    }
}
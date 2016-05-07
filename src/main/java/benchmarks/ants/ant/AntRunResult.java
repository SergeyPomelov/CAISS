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

package benchmarks.ants.ant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Class for storing data about finished ant's run.
 *
 * @author Sergey Pomelov on 28/04/2016.
 * @see RunningAnt
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class AntRunResult {
    @Nonnull
    private final TourData tourData;
    @Nonnull
    private final float[][] pheromonesDelta;
    @Nonnull
    private final String journal;

    AntRunResult(TourData tourData, float[][] pheromonesDelta, String journal) {
        this.tourData = tourData;
        this.pheromonesDelta = pheromonesDelta;
        this.journal = journal;
    }

    public boolean isSuccess() {
        return tourData.isSuccess();
    }

    @Nonnull
    public int[] getTour() {
        return tourData.getTour();
    }

    @Nonnegative
    public long getLength() {
        return tourData.getLength();
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField") // huge arrays, coping is worse case.
    @Nonnull
    public float[][] getPheromonesDelta() {
        return pheromonesDelta;
    }

    @Nonnull
    public String getJournal() {
        return journal;
    }
}

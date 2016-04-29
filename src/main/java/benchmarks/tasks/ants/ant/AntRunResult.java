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
 * @author Sergey Pomelov on 28/04/2016. Class for storing data about finished ant's run.
 * @see RunningAnt
 */
@Immutable
@ParametersAreNonnullByDefault
public final class AntRunResult {
    @Nonnull
    private final TourData tourData;
    @Nonnull
    private final double[][] pheromonesDelta;
    @Nonnull
    private final String journal;

    AntRunResult(TourData tourData, double[][] pheromonesDelta, String journal) {
        this.tourData = tourData;
        this.pheromonesDelta = pheromonesDelta.clone();
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

    @Nonnull
    public double[][] getPheromonesDelta() {
        return pheromonesDelta.clone();
    }

    @Nonnull
    public String getJournal() {
        return journal;
    }
}

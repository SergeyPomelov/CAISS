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

package benchmarks.tasks.ants.data;

import java.io.Serializable;

import javax.annotation.Nonnegative;

/**
 * @author Sergei Pomelov 07.01.2015. Interface of Class answering to size of problem (i.d. how mani
 *         vertexes there) and stating metrixes for distances betweeb the vertexes.
 */
public interface IDistancesData extends Serializable {
    /**
     * @param start   the start vertex
     * @param destiny the destiny vertex
     * @return the distance between them
     */
    @Nonnegative
    int getDist(int start, int destiny);

    /**
     * @return amount of possible vertexes here
     */
    @Nonnegative
    int getSize();
}

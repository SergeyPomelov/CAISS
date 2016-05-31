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

package benchmarks.ants.presets;

import javafx.util.Pair;

/**
 * @author Sergey Pomelov on 21/05/2016. Contains pair constants. The first element - the best known
 *         solution, sometimes notoptimal. The second element - task name, for load data from a
 *         name.tsp file.
 */
final class TSPTasksAndSolutions {

    static final Pair<Integer, String> WI29 = new Pair<>(27603, "wi29");
    static final Pair<Integer, String> QA194 = new Pair<>(9352, "qa194");
    static final Pair<Integer, String> XQG237 = new Pair<>(1019, "xqg237");
    static final Pair<Integer, String> UY734 = new Pair<>(79114, "uy734");
    static final Pair<Integer, String> LU980 = new Pair<>(11340, "lu980");
    static final Pair<Integer, String> XIT1083 = new Pair<>(3558, "xit1083");
    static final Pair<Integer, String> RW1621 = new Pair<>(26051, "rw1621");
    static final Pair<Integer, String> XQ1662 = new Pair<>(2513, "xql662");
    static final Pair<Integer, String> DCC1911 = new Pair<>(6396, "dcc1911");
    static final Pair<Integer, String> MU1979 = new Pair<>(86891, "mu1979");

    private TSPTasksAndSolutions() { /* constants holder */ }
}

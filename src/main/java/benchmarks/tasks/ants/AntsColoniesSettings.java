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

import javax.annotation.concurrent.Immutable;

import benchmarks.tasks.ants.data.IDistancesData;
import benchmarks.tasks.ants.data.TSPDistanceData;
import util.TimeUtil;

import static util.Constants.FS;

/**
 * Package-local constants and ACO settings holder.
 *
 * @author Sergey Pomelov on 28/04/2016.
 */
@Immutable
public final class AntsColoniesSettings {

    public static final int OPTIMUM = 27603; //  9352
    static final long RUN_PERIOD_NANOS = TimeUtil.secToNano(60);
    static final long SOLUTION_EXCHANGE_NANOS = TimeUtil.mlsToNano(1000);
    static final double EVAPORATION_COEFFICIENT = 0.1D;
    static final double INITIAL_TRAIL = 1.0D;
    // @formatter:off
    private static final String FILE = "wi29"; // qa194
    public static final IDistancesData GRAPH = // new FixedGraph();
            new TSPDistanceData(FS + "build" + FS + "resources" + FS + "main"
                    + FS + "tsp_data" + FS + FILE + ".tsp");
    // @formatter:on

    private AntsColoniesSettings() { /* constants holder */ }

}

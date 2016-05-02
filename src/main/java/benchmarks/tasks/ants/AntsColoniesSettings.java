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

import static util.Constants.FS;

/**
 * @author Sergey Pomelov on 28/04/2016. Package-local constants and ACO settings holder.
 */
@Immutable
final class AntsColoniesSettings { // @formatter:off
    
    static final double EVAPORATION_COEFFICIENT = 0.03D;
    static final double INITIAL_TRAIL = 1.0D;
    static final int SIZE = GRAPH.getSize();
    // public static final String tspFileName = "wi29";
    private static final String tspFileName = "qa194";
    static final IDistancesData GRAPH = // new FixedGraph();
            new TSPDistanceData(FS + "build" + FS + "resources" + FS + "main" + FS + "tsp_data" +
                    FS + tspFileName + ".tsp");

    private AntsColoniesSettings() { /* package local constants holder */ }
}

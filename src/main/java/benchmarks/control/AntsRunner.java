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

package benchmarks.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarks.tasks.ants.AntsColony;
import benchmarks.tasks.ants.IAntsOptimization;
import benchmarks.tasks.ants.data.IDistancesData;
import benchmarks.tasks.ants.data.TSPDistanceData;

import static util.Constants.FS;

/**
 * @author Sergey Pomelov on 13/04/2016.
 */
final class AntsRunner {

    private static final String tspFileName = "wi29"; //qa194
    private static final IDistancesData GRAPH = // new FixedGraph();
            new TSPDistanceData(FS + "build" + FS + "resources" + FS + "main"
                    + FS + "tsp_data" + FS + tspFileName + ".tsp");

    private static final Logger log = LoggerFactory.getLogger(AntsRunner.class);
    private static final long TEN_SEC_IN_NANOS = 200_000_000_000L;

    private AntsRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        final IAntsOptimization colony = new AntsColony(2);
        colony.run(System.nanoTime() + TEN_SEC_IN_NANOS);
        log.info(colony.getLog());
        // check();
        System.exit(0);
    }

    private static void check() {
        final String[] route = ("10>11>12>15>19>18>17>21>23>22>29>28>26>20>25>27>" +
                "24>16>14>13>9>7>3>4>8>5>1>2>6>").split(">");

        Double sum = 0.0D;
        Integer previousNodeNumber = null;
        for (String nodeNumberString : route) {
            final int nodeNumber = Integer.parseInt(nodeNumberString);
            double length = 0.0D;
            if (previousNodeNumber != null) {
                length = GRAPH.getDist(previousNodeNumber - 1, nodeNumber - 1);
                sum += length;
                log.info("{} -> {} = {}, total {}", previousNodeNumber, nodeNumber, length, sum);
            }

            previousNodeNumber = Integer.parseInt(nodeNumberString);
        }
        final int length = GRAPH.getDist(Integer.parseInt(route[0]) - 1,
                Integer.parseInt(route[route.length - 1]) - 1);
        sum += length;
        log.info("{} -> {} = {}, total {}", route[0], route[route.length - 1], length, sum);
        log.info("27616, sum = {}", sum);
    }
}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnegative;

import util.GNUCopyright;

/**
 * Runs ACO.
 * @author Sergey Pomelov on 13/04/2016.
 */
final class AntsRunner {

    private static final Logger log = LoggerFactory.getLogger(AntsRunner.class);
    private static final double PERCENTS = 100.0D;
    private static final AntsSettings SETTINGS = new AntsSettings();

    private AntsRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        Arrays.asList(1, 2, 3).forEach(
                coloniesAmount ->
                        Arrays.asList(1, 2, 3).forEach(
                                antsAmount ->
                                        runSeveralTimes(coloniesAmount, antsAmount, 5)));
        // check();
        System.exit(0);
    }

    private static void runSeveralTimes(@Nonnegative int coloniesAmount,
                                        @Nonnegative int antsPerColony,
                                        @Nonnegative int iterations) {
        final Collection<Long> results = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            results.add(AntsColonies.runCalculations(coloniesAmount, antsPerColony, SETTINGS));
        }
        log.info("{} colonies, x {} ants, accuracy: {}.", coloniesAmount, antsPerColony,
                results.stream().mapToDouble(result -> ((SETTINGS.getOptimum() * PERCENTS)
                        / result)).average
                        ());
    }

    private static void check() {
        final String[] route = ("10>11>12>15>19>18>17>21>23>22>29>28>26>20>25>27>" +
                "24>16>14>13>9>7>3>4>8>5>1>2>6>").split(">");

        Double sum = 0.0D;
        Integer previousNodeNumber = null;
        for (final String nodeNumberString : route) {
            final int nodeNumber = Integer.parseInt(nodeNumberString);
            if (previousNodeNumber != null) {
                final double dist = SETTINGS.getGraph().getDist(previousNodeNumber - 1,
                        nodeNumber - 1);
                sum += dist;
                log.info("{} -> {} = {}, total {}", previousNodeNumber, nodeNumber, dist, sum);
            }

            previousNodeNumber = Integer.parseInt(nodeNumberString);
        }
        final int length = SETTINGS.getGraph().getDist(Integer.parseInt(route[0]) - 1,
                Integer.parseInt(route[route.length - 1]) - 1);
        sum += length;
        log.info("{} -> {} = {}, total {}", route[0], route[route.length - 1], length, sum);
        log.info("27616, sum = {}", sum);
    }
}

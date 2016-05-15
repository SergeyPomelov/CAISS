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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;

import javax.annotation.Nonnegative;

import javafx.util.Pair;
import util.GNUCopyright;

import static benchmarks.ants.ColonyRunResult.compileOverallResults;
import static util.Constants.LS;

/**
 * Runs ACO.
 *
 * @author Sergey Pomelov on 13/04/2016.
 */
final class AntsRunner {

    private static final Logger log = LoggerFactory.getLogger(AntsRunner.class);
    private static final DecimalFormat format = new DecimalFormat("##.####");
    private static final double PERCENTS = 100.0D;
    private static final AntsSettings SETTINGS = new AntsSettings();
    private static final Collection<Pair<String, ColonyRunResult>> overallResults =
            new ArrayList<>(0);

    private AntsRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        doTest();
        //do1();
        printData();
        //check();
        System.exit(0);
    }

    private static void printData() {
        final StringBuilder out = new StringBuilder(0);
        fillDataForValue(out, "accuracy", Pair::getKey);
        fillDataForValue(out, "runs", result -> String.valueOf(result.getValue().getAntRuns()));
        fillDataForValue(out, "exchanges", result ->
                String.valueOf(result.getValue().getExchanges()));
        fillDataForValue(out, "runSpeed", result ->
                String.valueOf(result.getValue().getAvgAntsRunNs()));
        fillDataForValue(out, "exchangeSpeed", result ->
                String.valueOf(result.getValue().getAvgExchangeNs()));
        log.info(out.toString());
    }

    private static void fillDataForValue(StringBuilder out, String label,
                                         Function<Pair<String, ColonyRunResult>, String>
                                                 valueExtractor) {
        out.append(label).append(" = {");
        boolean firstValue = true;
        for (Pair<String, ColonyRunResult> runResult : overallResults) {
            addValue(out, firstValue, runResult.getValue(), valueExtractor.apply(runResult));
            firstValue = false;
        }
        out.append("};").append(LS);
    }

    private static StringBuilder addValue(StringBuilder out, boolean firstValue,
                                          ColonyRunResult runResult,
                                          String value) {
        if (!firstValue) {
            out.append(',');
        }
        return out.append('{').append(runResult.getAnts()).append(',')
                .append(runResult.getColonies()).append(',')
                .append(value)
                .append('}');
    }

    private static void doTest() {
        runSeveralTimes(2, 2, 2, true);
        log.info("warm up end.");
        Arrays.asList(2, 4, 6, 8, 10, 12, 14).forEach(
                coloniesAmount ->
                        Arrays.asList(2, 4, 6, 8, 10, 12, 14).forEach(
                                antsAmount ->
                                        runSeveralTimes(coloniesAmount, antsAmount, 5, false)));
    }

    private static void do1() {
        runSeveralTimes(2, 2, 2, true);
        log.info("warm up end.");
        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).forEach(
                coloniesAmount ->
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).forEach(
                                antsAmount ->
                                        runSeveralTimes(coloniesAmount, antsAmount, 5, false)));
    }

    private static void runSeveralTimes(@Nonnegative int coloniesAmount,
                                        @Nonnegative int antsPerColony,
                                        @Nonnegative int iterations,
                                        boolean warmUpRan) {
        final List<ColonyRunResult> results = new ArrayList<>(iterations);
        final Collection<Long> tourLengths = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            final ColonyRunResult output = AntsColonies.runCalculations(coloniesAmount,
                    antsPerColony, SETTINGS);
            results.add(output);
            tourLengths.add(output.getResult());
        }
        saveAndLogResults(coloniesAmount, antsPerColony, tourLengths, results, warmUpRan);
    }

    private static void saveAndLogResults(int coloniesAmount, int antsPerColony,
                                          Collection<Long> tourLengths,
                                          List<ColonyRunResult> results,
                                          boolean warmUpRan) {
        final ColonyRunResult totalResult = compileOverallResults(results,
                coloniesAmount, antsPerColony);
        final String percentString = formatPercents(tourLengths);
        if (!warmUpRan) {
            overallResults.add(new Pair<>(percentString, totalResult));
        }
        log.info("{} colonies, x {} ants, accuracy: {}, performance: {}.", coloniesAmount,
                antsPerColony, percentString, totalResult.info());
    }

    private static synchronized String formatPercents(Collection<Long> tourLengths) {
        final OptionalDouble optionalAccuracyPercent =
                tourLengths.stream().mapToDouble(result ->
                        ((SETTINGS.getOptimum() * PERCENTS) / result)).average();
        return format.format(optionalAccuracyPercent.orElse(0.0D));
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

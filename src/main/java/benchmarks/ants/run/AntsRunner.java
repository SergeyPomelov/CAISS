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

package benchmarks.ants.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalDouble;

import javax.annotation.Nonnegative;

import benchmarks.ants.AntsColonies;
import benchmarks.ants.AntsSettings;
import benchmarks.ants.colony.ColonyRunResult;
import javafx.util.Pair;
import util.GNUCopyright;

import static benchmarks.ants.run.ColonyResultsCompiler.compileOverallResults;
import static benchmarks.ants.run.MathematicaFormatter.printData;

/**
 * Runs ACO.
 *
 * @author Sergey Pomelov on 13/04/2016.
 */
@SuppressWarnings("FeatureEnvy")
final class AntsRunner {

    private static final Logger log = LoggerFactory.getLogger(AntsRunner.class);
    private static final DecimalFormat format = new DecimalFormat("##.####");
    private static final double PERCENTS = 100.0D;
    private static final Collection<Pair<String, ColonyRunResult>> overallResults =
            new ArrayList<>(0);

    private AntsRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        warmUp(Presets.WARM_UP.getPreset());
        runExperiment(Presets.LU980_6X6_3_2H30M.getPreset());
        runExperiment(Presets.XQL662_6X6_3_2H30M.getPreset());
        runExperiment(Presets.DCC911_6X6_3_2H30M.getPreset());
        runExperiment(Presets.MU1979_6X6_3_2H30M.getPreset());
        System.exit(0);
    }

    private static void runExperiment(AntsExperimentPreset preset) {
        preset.getColonies().forEach(coloniesAmount ->
                preset.getAnts().forEach(antsAmount ->
                        runSeveralTimes(preset,
                                coloniesAmount, antsAmount,
                                preset.getRunsForAverageResult(), false)));
        printData(overallResults);
    }

    private static void warmUp(AntsExperimentPreset preset) {
        runSeveralTimes(preset, 2, 2, 2, true);
        log.info("warm up end.");
    }

    private static void runSeveralTimes(AntsExperimentPreset preset,
                                        @Nonnegative int coloniesAmount,
                                        @Nonnegative int antsPerColony,
                                        @Nonnegative int iterations,
                                        boolean warmUpRan) {
        final Collection<ColonyRunResult> results = new ArrayList<>(iterations);
        final Collection<Long> tourLengths = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            final ColonyRunResult output = AntsColonies.runCalculations(coloniesAmount,
                    antsPerColony, preset.getSettings());
            results.add(output);
            tourLengths.add(output.getResult());
        }
        saveAndLogResults(preset, coloniesAmount, antsPerColony, tourLengths, results, warmUpRan);
    }

    private static void saveAndLogResults(AntsExperimentPreset preset,
                                          int coloniesAmount, int antsPerColony,
                                          Collection<Long> tourLengths,
                                          Collection<ColonyRunResult> results,
                                          boolean warmUpRan) {
        final ColonyRunResult totalResult = compileOverallResults(results,
                coloniesAmount, antsPerColony);
        final String percentString = formatPercents(preset.getSettings(), tourLengths);
        if (!warmUpRan) {
            overallResults.add(new Pair<>(percentString, totalResult));
        }
        log.info("{}, {} runs. {} colonies, x {} ants, accuracy: {}, performance: {}.",
                preset.getData().getValue(), preset.getRunsForAverageResult(),
                coloniesAmount, antsPerColony, percentString, totalResult.info());
    }

    private static synchronized String formatPercents(AntsSettings settings,
                                                      Collection<Long> tourLengths) {
        final OptionalDouble optionalAccuracyPercent =
                tourLengths.stream().mapToDouble(result ->
                        ((settings.getOptimum() * PERCENTS) / result)).average();
        return format.format(optionalAccuracyPercent.orElse(0.0D));
    }

    /*private static void check() {
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
    } */
}

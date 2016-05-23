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

import benchmarks.ants.colonies.AntsColonies;
import benchmarks.ants.colonies.AntsExperimentData;
import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colonies.colony.ColonyRunResult;
import benchmarks.ants.presets.AntsExperimentSeriesPreset;
import javafx.util.Pair;
import util.GNUCopyright;

import static benchmarks.ants.colonies.ColonyResultsCompiler.avgResult;
import static benchmarks.ants.presets.ExperimentsSeriesPresetsBuilders.QA194_8X8_1_10H;
import static benchmarks.ants.presets.ExperimentsSeriesPresetsBuilders.WARM_UP;
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
        warmUp(WARM_UP.createAntsExperimentPreset());
        runExperiment(QA194_8X8_1_10H.createAntsExperimentPreset());
        System.exit(0);
    }

    private static void runExperiment(AntsExperimentSeriesPreset preset) {
        preset.getColonies().forEach(coloniesAmount ->
                preset.getAnts().forEach(antsAmount ->
                        runSeveralTimes(
                                generateData(preset, coloniesAmount, antsAmount), false)));
        printData(overallResults);
    }

    private static void warmUp(AntsExperimentSeriesPreset preset) {
        runSeveralTimes(generateData(preset, 2, 2), true);
        log.info("warm up end.");
    }

    private static AntsExperimentData generateData(AntsExperimentSeriesPreset preset,
                                                   int colonies, int ants) {
        return new AntsExperimentData(colonies, ants, preset);
    }

    private static void runSeveralTimes(AntsExperimentData data,
                                        boolean warmUpRan) {
        final AntsExperimentSeriesPreset preset = data.getPreset();
        final int iterations = preset.getRunsForAverageResult();
        final Collection<ColonyRunResult> results = new ArrayList<>(iterations);
        final Collection<Long> tourLengths = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            final ColonyRunResult output = AntsColonies.runCalculations(data);
            results.add(output);
            tourLengths.add(output.getResult());
        }
        saveAndLogResults(preset, data.getColonies(), data.getAnts(),
                tourLengths, results, warmUpRan);
    }

    private static void saveAndLogResults(AntsExperimentSeriesPreset preset,
                                          int coloniesAmount, int antsPerColony,
                                          Collection<Long> tourLengths,
                                          Collection<ColonyRunResult> results,
                                          boolean warmUpRan) {
        final ColonyRunResult totalResult = avgResult(results, coloniesAmount, antsPerColony);
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
}

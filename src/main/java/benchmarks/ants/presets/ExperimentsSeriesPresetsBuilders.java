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

import java.util.Arrays;
import java.util.Collections;

import static benchmarks.ants.presets.TSPTasksAndSolutions.DCC1911;
import static benchmarks.ants.presets.TSPTasksAndSolutions.LU980;
import static benchmarks.ants.presets.TSPTasksAndSolutions.MU1979;
import static benchmarks.ants.presets.TSPTasksAndSolutions.QA194;
import static benchmarks.ants.presets.TSPTasksAndSolutions.RW1621;
import static benchmarks.ants.presets.TSPTasksAndSolutions.UY734;
import static benchmarks.ants.presets.TSPTasksAndSolutions.WI29;
import static benchmarks.ants.presets.TSPTasksAndSolutions.XIT1083;
import static benchmarks.ants.presets.TSPTasksAndSolutions.XQ1662;
import static benchmarks.ants.presets.TSPTasksAndSolutions.XQG237;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
@SuppressWarnings("unused")
public final class ExperimentsSeriesPresetsBuilders {

    public static final AntsExperimentSeriesPresetBuilder WARM_UP =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Collections.singletonList(2))
                    .setAnts(Collections.singletonList(2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(1);

    public static final AntsExperimentSeriesPresetBuilder WI29_2X2_2_3M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(WI29)
                    .setColonies(Arrays.asList(1, 2))
                    .setAnts(Arrays.asList(1, 2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(3);
    public static final AntsExperimentSeriesPresetBuilder XQQ237_2X2_2_3M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(XQG237)
                    .setColonies(Arrays.asList(1, 2))
                    .setAnts(Arrays.asList(1, 2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(3);

    public static final AntsExperimentSeriesPresetBuilder UY734_2X2_2_3M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(UY734)
                    .setColonies(Arrays.asList(1, 2))
                    .setAnts(Arrays.asList(1, 2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(3);
    public static final AntsExperimentSeriesPresetBuilder XIT1083_2X2_2_3M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(XIT1083)
                    .setColonies(Arrays.asList(1, 2))
                    .setAnts(Arrays.asList(1, 2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(3);
    public static final AntsExperimentSeriesPresetBuilder RW1621_2X2_2_3M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(RW1621)
                    .setColonies(Arrays.asList(1, 2))
                    .setAnts(Arrays.asList(1, 2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInMinutes(3);

    public static final AntsExperimentSeriesPresetBuilder XQG237_6X6_3_2H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(XQG237)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(120);
    public static final AntsExperimentSeriesPresetBuilder UY734_6X6_3_2H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(UY734)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(120);
    public static final AntsExperimentSeriesPresetBuilder XIT1083_6X6_3_2H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(XIT1083)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(120);
    public static final AntsExperimentSeriesPresetBuilder RW1621_6X6_3_2H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(RW1621)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(120);

    public static final AntsExperimentSeriesPresetBuilder QA194_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);
    public static final AntsExperimentSeriesPresetBuilder UY734_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(UY734)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);
    public static final AntsExperimentSeriesPresetBuilder LU980_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(LU980)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);
    public static final AntsExperimentSeriesPresetBuilder XQL662_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(XQ1662)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);

    public static final AntsExperimentSeriesPresetBuilder DCC911_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(DCC1911)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);
    public static final AntsExperimentSeriesPresetBuilder MU1979_6X6_3_2H30M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(MU1979)
                    .setColonies(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setAnts(Arrays.asList(1, 2, 3, 4, 5, 6))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(150);

    public static final AntsExperimentSeriesPresetBuilder QA194_8X8_1_10H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Arrays.asList(2, 3, 4, 5, 6, 7, 8))
                    .setAnts(Arrays.asList(2, 3, 4, 5, 6, 7, 8))
                    .setRunsForAverageResult(1)
                    .setOverallRunTimeInMinutes(10 * 60);

    public static final AntsExperimentSeriesPresetBuilder QA194_S2_16X16_1_10H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16))
                    .setAnts(Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16))
                    .setRunsForAverageResult(1)
                    .setOverallRunTimeInMinutes(10 * 60);

    public static final AntsExperimentSeriesPresetBuilder QA194_S4_64X64_1_10H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Arrays.asList(128, 112, 96, 80, 64, 48, 32, 16))
                    .setAnts(Arrays.asList(128, 112, 96, 80, 64, 48, 32, 16))
                    .setRunsForAverageResult(1)
                    .setOverallRunTimeInMinutes(10 * 60);

    public static final AntsExperimentSeriesPresetBuilder QA194_4X4_4_10H =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(QA194)
                    .setColonies(Arrays.asList(1, 2, 3, 4))
                    .setAnts(Arrays.asList(1, 2, 3, 4))
                    .setRunsForAverageResult(4)
                    .setOverallRunTimeInMinutes(10 * 16 * 4);

    public static final AntsExperimentSeriesPresetBuilder DCC911_3X3_3_18M =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(DCC1911)
                    .setColonies(Arrays.asList(1, 2, 3))
                    .setAnts(Arrays.asList(1, 2, 3))
                    .setRunsForAverageResult(1)
                    .setOverallRunTimeInMinutes(6);

    public static final AntsExperimentSeriesPresetBuilder DCC911_TEST =
            new AntsExperimentSeriesPresetBuilder()
                    .setData(DCC1911)
                    .setColonies(Collections.singletonList(4))
                    .setAnts(Collections.singletonList(4))
                    .setRunsForAverageResult(3)
                    .setOverallRunTimeInMinutes(10);

    private ExperimentsSeriesPresetsBuilders() { /* utility holder */ }
}

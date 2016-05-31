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

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import benchmarks.ants.colonies.AntsColonies;
import benchmarks.ants.colonies.AntsExperimentData;
import benchmarks.ants.colonies.AntsSettings;
import benchmarks.ants.colonies.colony.ColonyRunResult;
import benchmarks.ants.presets.AntsExperimentSeriesPreset;
import benchmarks.ants.presets.AntsExperimentSeriesPresetBuilder;
import javafx.util.Pair;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
public class AntsColoniesTest {

    private static AntsSettings SETTINGS;

    private static AntsExperimentSeriesPreset WI29_2X2_2_3M;

    static {
        try {
            WI29_2X2_2_3M = new AntsExperimentSeriesPresetBuilder()
                    .setData(new Pair<>(27603, "wi29"))
                    .setColonies(Collections.singletonList(2))
                    .setAnts(Collections.singletonList(2))
                    .setRunsForAverageResult(2)
                    .setOverallRunTimeInNanos(1000L).createAntsExperimentPreset();
            SETTINGS = new AntsSettings(27603, "wi29",
                    1_000_000L, 100L, 0.01F, 1.0F);
        } catch (IOException e) {
            fail();
        }
    }

    private static final ColonyRunResult result = AntsColonies.runCalculations(
            new AntsExperimentData(2, 2, WI29_2X2_2_3M));

    @Test
    public void paramsPassedToEnd() {
        assertEquals(2L, result.getAnts());
        assertEquals(2L, result.getColonies());
    }

    @Test
    public void existsResult() {
        assertNotNull(result);
        assertTrue(result.getResult() < Long.MAX_VALUE);
    }

    @Test
    public void antsSmoke() {
        assertTrue(result.getAntRuns() > 0L);
        assertTrue(result.getAvgAntsRunNs() > 0L);
    }

    @Ignore
    @Test
    public void exchangesSmoke() {
        assertTrue(result.getExchanges() > 0L);
        assertTrue(result.getAvgExchangeNs() > 0L);
    }
}

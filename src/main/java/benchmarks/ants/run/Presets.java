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

import java.util.Arrays;
import java.util.Collections;

import javafx.util.Pair;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
enum Presets {

    // private static final Pair<Integer, String> data = new Pair<>(27603, "wi29");
    // private static final Pair<Integer, String> data = new Pair<>(6656, "dj38");
    // private static final Pair<Integer, String> data = new Pair<>(564, "xqf131");
    // private static final Pair<Integer, String> data = new Pair<>(9352, "qa194");
    // private static final Pair<Integer, String> data = new Pair<>(1019, "xqg237");
    // private static final Pair<Integer, String> data = new Pair<>(79114, "uy734");
    // private static final Pair<Integer, String> data = new Pair<>(3558, "xit1083");
    // private static final Pair<Integer, String> data = new Pair<>(26051, "rw1621");

    WARM_UP(new AntsExperimentPreset(new Pair<>(9352, "qa194"),
            Collections.singletonList(2), Collections.singletonList(2), 2, 1)),

    WI29_2X2_2_3M(new AntsExperimentPreset(new Pair<>(27603, "wi29"),
            Arrays.asList(1, 2), Arrays.asList(1, 2), 2, 3)),
    XQQ237_2X2_2_3M(new AntsExperimentPreset(new Pair<>(1019, "xqg237"),
            Arrays.asList(1, 2), Arrays.asList(1, 2), 2, 3)),

    UY734_2X2_2_3M(new AntsExperimentPreset(new Pair<>(79114, "uy734"),
            Arrays.asList(1, 2), Arrays.asList(1, 2), 2, 3)),
    XIT1083_2X2_2_3M(new AntsExperimentPreset(new Pair<>(3558, "xit1083"),
            Arrays.asList(1, 2), Arrays.asList(1, 2), 2, 3)),
    RW1621_2X2_2_3M(new AntsExperimentPreset(new Pair<>(26051, "rw1621"),
            Arrays.asList(1, 2), Arrays.asList(1, 2), 2, 3)),

    XQQ237_6X6_3_2H(new AntsExperimentPreset(new Pair<>(1019, "xqg237"),
            Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 6), 3, 120)),
    UY734_6X6_3_2H(new AntsExperimentPreset(new Pair<>(79114, "uy734"),
            Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 6), 3, 120)),
    XIT1083_6X6_3_2H(new AntsExperimentPreset(new Pair<>(3558, "xit1083"),
            Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 6), 3, 120)),
    RW1621_6X6_3_2H(new AntsExperimentPreset(new Pair<>(26051, "rw1621"),
            Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 6), 3, 120));

    private final AntsExperimentPreset preset;

    Presets(AntsExperimentPreset preset) {
        this.preset = preset;
    }

    public AntsExperimentPreset getPreset() {
        return preset;
    }
}

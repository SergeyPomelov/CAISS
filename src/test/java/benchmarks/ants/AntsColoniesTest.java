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

import org.junit.Test;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
public class AntsColoniesTest {

    private static final AntsSettings SETTINGS = new AntsSettings(27603, 1000, 100, 0.1F,
            1.0F, "wi29");

    @Test
    public void antsSmoke() {
        AntsColonies.runCalculations(2, 2, SETTINGS);
    }
}

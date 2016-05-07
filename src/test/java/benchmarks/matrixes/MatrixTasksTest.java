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

package benchmarks.matrixes;

import org.junit.Test;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
public class MatrixTasksTest {

    private static final int ITERATIONS = 1;
    private static final int SQUARE_ROOT_OF_DATA_SIZE = 2;
    private static final int CALCULATIONS_MULTIPLICATOR = 2;

    @Test
    public void matrixPowSmoke() {
        MatrixTasks.matrixPowInner(2, ITERATIONS,
                SQUARE_ROOT_OF_DATA_SIZE, CALCULATIONS_MULTIPLICATOR);
    }

    @Test
    public void memoryAllocSmoke() {
        MatrixTasks.memoryAlloc(3);
    }
}

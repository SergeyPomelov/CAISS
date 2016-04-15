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

package benchmarks.tasks.martixes;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Sergey Pomelov on 06.04.16.
 */
final class DataProcessor {

    private DataProcessor() { /* utility class */ }

    static void processArray(byte[][] data, int taskMultiplicator) {
        final int cols = data.length;
        final int rows = data[0].length;
        for (int loop = 0; loop < taskMultiplicator; loop++) {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    data[i][j] *= 2;
                }
            }
        }
    }

    static byte[][] generateRandomArray(int rows, int cols) {
        final byte[][] out = new byte[rows][cols];
        final Random random = new SecureRandom();

        for (int i = 0; i < rows; i++) {
            //noinspection ObjectAllocationInLoop, by design
            final byte[] row = new byte[cols];
            random.nextBytes(row);
            out[i] = row;
        }
        return out;
    }
}

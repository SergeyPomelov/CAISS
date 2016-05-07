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

import com.google.common.annotations.VisibleForTesting;

import static util.Restrictions.ifNegativeFail;

/**
 * @author Sergey Pomelov on 06/04/16.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
final class MatrixTasks {

    private static final int ITERATIONS = 1;
    private static final int SQUARE_ROOT_OF_DATA_SIZE = 128;
    private static final int CALCULATIONS_MULTIPLICATOR = 100000;

    private MatrixTasks() { /* utility class*/ }

    static void matrixPow(int threads) {
        matrixPowInner(ifNegativeFail(threads),
                ITERATIONS, SQUARE_ROOT_OF_DATA_SIZE, CALCULATIONS_MULTIPLICATOR);
    }

    @VisibleForTesting
    static void matrixPowInner(int threads, int runs, int problemSize, int taskMultiplicator) {
        TasksRunner.runExperiment(threads, runs, problemSize, taskMultiplicator);
    }

    static byte[][] memoryAlloc(int size) {
        return new byte[ifNegativeFail(size)][ifNegativeFail(size)];
    }
}

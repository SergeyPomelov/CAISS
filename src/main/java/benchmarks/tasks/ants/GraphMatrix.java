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

package benchmarks.tasks.ants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * @author Sergei Pomelov 04.01.2015.
 */
@Immutable
public final class GraphMatrix implements IGraph {

    private static final long serialVersionUID = 4778301831516758978L;
    private static final Random rand = new SecureRandom();
    @Nonnegative
    private static final int SIZE = 256;
    @Nonnegative
    private static final int MAX_LENGTH = 10; // maximum value in cells.

    @Nonnull
    private final List<List<Integer>> matrix = new ArrayList<>(SIZE);

    GraphMatrix() {
        generateLowerHalfRandom();
        symmetry();
        zeroDiagonal();
        check();
    }

    @Override
    @Nonnegative
    public int getDist(int start, int destiny) {
        if ((start >= SIZE) || (destiny >= SIZE)) {
            throw new IllegalArgumentException("GraphMatrix size overflowed! " +
                    start + ' ' + destiny);
        } else if (start == destiny) {
            return Integer.MAX_VALUE;
        } else {
            final Integer out = matrix.get(start).get(destiny);
            return (out <= 0) ? Integer.MAX_VALUE : out;
        }
    }

    @Override
    @Nonnegative
    public int getSize() {
        return SIZE;
    }

    private void generateLowerHalfRandom() {
        for (int i = 0; i < SIZE; i++) {
            //noinspection ObjectAllocationInLoop, this is by design
            final List<Integer> list = new ArrayList<>(SIZE);
            for (int j = 0; j <= i; j++) {
                // noinspection  no loss here while MAX_LENGTH <
                // Integer.Max
                list.add(rand.nextInt(MAX_LENGTH));
            }
            matrix.add(list);
        }
    }

    private void symmetry() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (j > i) {
                    matrix.get(i).add(j, matrix.get(j).get(i));
                }
            }
        }
    }

    private void zeroDiagonal() {
        for (int i = 0; i < SIZE; i++) {
            matrix.get(i).set(i, 0);
        }
    }

    private void check() {
        for (int i = 0; i < SIZE; i++) {
            final List<Integer> list = matrix.get(i);
            final int subSize = list.size();
            if (subSize != SIZE) {
                throw new IllegalStateException("Non square matrix! " + i);
            }
            for (int j = 0; j < i; j++) {
                if (getDist(i, j) != getDist(j, i)) {
                    throw new IllegalStateException("Non symmetric matrix! " + i + ' ' + j);
                }
            }
        }
    }
}

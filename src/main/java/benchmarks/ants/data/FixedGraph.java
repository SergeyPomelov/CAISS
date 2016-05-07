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

package benchmarks.ants.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import javafx.util.Pair;

/**
 * Class for manual filling a simple test data for ACO.
 * @author Sergey Pomelov on 27/04/2016.
 */
@Immutable
public final class FixedGraph implements IDistancesData {

    private static final long serialVersionUID = -42424710759314150L;
    private final Map<Integer, List<Pair<Integer, Integer>>> nodes = new HashMap<>(8);

    public FixedGraph() {
        addSymmetricPath(1, 2, 1);
        addSymmetricPath(1, 3, 1);
        addSymmetricPath(1, 4, 2);
        addSymmetricPath(2, 5, 3);
        addSymmetricPath(3, 4, 1);
        addSymmetricPath(3, 7, 2);
        addSymmetricPath(4, 6, 1);
        addSymmetricPath(5, 7, 1);
        addSymmetricPath(6, 7, 1);
    }

    @Override
    public int getDist(int start, int destiny) {
        if (nodes.containsKey(start + 1)) {
            return searchInDistList(nodes.get(start + 1), destiny + 1);
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public int getSize() {
        return nodes.size();
    }

    private static int searchInDistList(Iterable<Pair<Integer, Integer>> pairs, int destiny) {
        for (final Pair<Integer, Integer> pair : pairs) {
            if (pair.getKey() == destiny) {
                return pair.getValue();
            }
        }
        return Integer.MAX_VALUE;
    }

    private void addSymmetricPath(int start, int end, int length) {
        addOneWayPath(start, end, length);
        addOneWayPath(end, start, length);
    }

    private void addOneWayPath(int start, int end, int length) {
        if (nodes.containsKey(start)) {
            final List<Pair<Integer, Integer>> node = nodes.get(start);
            node.add(new Pair<>(end, length));
        } else {
            final List<Pair<Integer, Integer>> node = new ArrayList<>(1);
            node.add(new Pair<>(end, length));
            nodes.put(start, nodes.getOrDefault(start, node));
        }
    }

}

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

import com.google.common.base.Charsets;

import com.sun.istack.internal.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;

import javafx.util.Pair;

/**
 * Class for loading distances data from .tsp form.
 *
 * @author Sergey Pomelov on 26/04/2016.
 */
@ThreadSafe
public final class TSPDistanceData implements IDistancesData {

    private static final Logger log = LoggerFactory.getLogger(TSPDistanceData.class);

    private static final long serialVersionUID = 6519359333995572903L;
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    @NotNull
    private int[][] distances = new int[1][1];
    private int size = 0;

    /**
     * @param fileLocation - is a relative location, appended to the user.dir, root of the project
     */
    public TSPDistanceData(String fileLocation) {
        try (BufferedReader br = buildBufferReader(fileLocation)) {
            distances = readData(br);
            size = distances.length;
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException during construction!", e);
        } catch (IOException e) {
            log.error("IOException during construction!", e);
        }
    }

    @Nonnegative
    @Override
    public int getDist(int start, int destiny) {
        return distances[start][destiny];
    }

    @Nonnegative
    @Override
    public int getSize() {
        return size;
    }

    private static BufferedReader buildBufferReader(String fileLocation)
            throws FileNotFoundException {
        final FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +
                fileLocation);
        final InputStreamReader isr = new InputStreamReader(fis, Charsets.UTF_8);
        return new BufferedReader(isr);
    }

    private static int[][] readData(BufferedReader br) throws IOException {
        boolean nodesReadStart = false;
        final List<Pair<Float, Float>> tempNodes = new ArrayList<>(0);
        // All right, lines from reader are finite.
        //noinspection ForLoopWithMissingComponent,MethodCallInLoopCondition
        for (String line; (((line = br.readLine())) != null) && !"EOF".equals(line); ) {
            if (nodesReadStart) {
                readNode(line, tempNodes);
            } else if ("NODE_COORD_SECTION".equals(line)) {
                nodesReadStart = true;
            }
        }
        return convertToDistArray(tempNodes);
    }

    private static int[][] convertToDistArray(List<Pair<Float, Float>> tempNodes) {
        final int[][] tempDistArray = new int[tempNodes.size()][tempNodes.size()];
        final int size = tempNodes.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempDistArray[i][j] = getDistInner(i, j, tempNodes);
            }
        }
        return tempDistArray;
    }

    //Rounding distance as in TSPLib for optimal solution from there matching.
    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Nonnegative
    private static int getDistInner(int start, int destiny, List<Pair<Float, Float>> tempNodes) {
        if (start == destiny) {
            return Integer.MAX_VALUE;
        }
        return (int) Math.round(StrictMath.hypot(
                getX(destiny, tempNodes) - getX(start, tempNodes),
                getY(destiny, tempNodes) - getY(start, tempNodes)));
    }

    private static void readNode(CharSequence line, Collection<Pair<Float, Float>> tempNodes) {
        final String[] formatParts = WHITESPACE.split(line);
        if (formatParts.length == 3) {
            tempNodes.add(new Pair<>(Float.valueOf(formatParts[1]), Float.valueOf(formatParts[2])));
        } else {
            log.error("Illegal .tsp nodes format line {}!", line);
        }
    }

    private static float getX(int node, List<Pair<Float, Float>> tempNodes) {
        return tempNodes.get(node).getKey();
    }

    private static float getY(int node, List<Pair<Float, Float>> tempNodes) {
        return tempNodes.get(node).getValue();
    }
}

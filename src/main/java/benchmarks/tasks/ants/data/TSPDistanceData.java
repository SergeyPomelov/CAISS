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

package benchmarks.tasks.ants.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.concurrent.Immutable;

import javafx.util.Pair;

/**
 * @author Sergey Pomelov on 26/04/2016. Class for loading distances data from .tsp forma.
 */
@Immutable
public final class TSPDistanceData implements IDistancesData {

    private static final Logger log = LoggerFactory.getLogger(TSPDistanceData.class);

    private static final long serialVersionUID = 6519359333995572903L;
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    private final List<Pair<Float, Float>> nodes = new ArrayList<>(194);

    /**
     * @param fileLocation - is a relative location, appended to the user.dir, root of the project
     */
    public TSPDistanceData(String fileLocation) {
        try (BufferedReader br =
                     new BufferedReader(new FileReader(System.getProperty("user.dir") +
                             fileLocation))) {
            boolean nodesReadStart = false;
            // All right, lines from reader are finite.
            //noinspection ForLoopWithMissingComponent,MethodCallInLoopCondition
            for (String line; (((line = br.readLine())) != null) && !"EOF".equals(line); ) {
                if (nodesReadStart) {
                    readNode(line);
                } else if ("NODE_COORD_SECTION".equals(line)) {
                    nodesReadStart = true;
                }
            }
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException during construction!", e);
        } catch (IOException e) {
            log.error("IOException during construction!", e);
        }
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Override
    public int getDist(int start, int destiny) {
        if (start == destiny) {
            return Integer.MAX_VALUE;
        }
        //noinspection NumericCastThatLosesPrecision, rounding distance as in TSPLib for optimal
        return (int) Math.round(StrictMath.hypot( //solution from there matching
                getX(destiny) - getX(start),
                getY(destiny) - getY(start)));
    }

    @Override
    public int getSize() {
        return nodes.size();
    }

    private void readNode(CharSequence line) {
        final String[] formatParts = WHITESPACE.split(line);
        if (formatParts.length == 3) {
            nodes.add(new Pair<>(Float.valueOf(formatParts[1]), Float.valueOf(formatParts[2])));
        } else {
            log.error("Illegal .tsp nodes format line {}!", line);
        }
    }

    private float getX(int node) {
        return nodes.get(node).getKey();
    }

    private float getY(int node) {
        return nodes.get(node).getValue();
    }
}

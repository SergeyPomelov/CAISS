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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.function.Function;

import benchmarks.ants.colonies.colony.ColonyRunResult;
import javafx.util.Pair;

import static util.Constants.LS;

/**
 * @author Sergey Pomelov on 17/05/2016.
 */
final class MathematicaFormatter {

    private static final Logger log = LoggerFactory.getLogger(MathematicaFormatter.class);

    private MathematicaFormatter() { /* package-local utility class */ }

    @SuppressWarnings("FeatureEnvy")
    static void printDataThenClearSource(Collection<Pair<String, ColonyRunResult>> overallResults) {
        final StringBuilder out = new StringBuilder(0);
        fillDataForValue(overallResults, out, "accuracy", Pair::getKey);
        fillDataForValue(overallResults, out, "runs", result ->
                String.valueOf(result.getValue().getAntRuns()));
        fillDataForValue(overallResults, out, "exchanges", result ->
                String.valueOf(result.getValue().getExchanges()));
        fillDataForValue(overallResults, out, "runSpeed", result ->
                String.valueOf(result.getValue().getAvgAntsRunNs()));
        fillDataForValue(overallResults, out, "exchangeSpeed", result ->
                String.valueOf(result.getValue().getAvgExchangeNs()));
        log.info(out.toString());
        overallResults.clear();
    }

    static void fillDataForValue(Iterable<Pair<String, ColonyRunResult>> overallResults,
                                 StringBuilder out, String label,
                                 Function<Pair<String, ColonyRunResult>, String>
                                                 valueExtractor) {
        out.append(label).append(" = {");
        boolean firstValue = true;
        for (Pair<String, ColonyRunResult> runResult : overallResults) {
            addValue(out, firstValue, runResult.getValue(), valueExtractor.apply(runResult));
            firstValue = false;
        }
        out.append("};").append(LS);
    }

    private static void addValue(StringBuilder out, boolean firstValue,
                                 ColonyRunResult runResult, String value) {
        if (!firstValue) {
            out.append(',');
        }
        out.append('{').append(runResult.getAnts()).append(',')
                .append(runResult.getColonies()).append(',')
                .append(value)
                .append('}');
    }

}

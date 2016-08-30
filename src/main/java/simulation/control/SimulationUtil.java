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

package simulation.control;

import simulation.structures.commons.StructureElement;

import static util.Constants.LS;

/**
 * @author Sergey Pomelov on 26/05/2016.
 */
final class SimulationUtil {

    private SimulationUtil() { /* package-local utility class */ }

    static void addElementsListInfo(Iterable<? extends StructureElement> elements,
                                    StringBuilder out, String label) {
        addDivider(out, label);
        for (final StructureElement element : elements) {
            out.append(element.info());
        }
    }

    static void addDivider(StringBuilder out, String label) {
        out.append(LS).append(LS).append("===================================================")
                .append(LS).append(label).append(LS);
    }
}

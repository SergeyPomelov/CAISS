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

package simulation.structures.algorithm;

import javax.annotation.Nonnull;

import simulation.structures.commons.ComponentType;
import simulation.structures.commons.StructureElement;

/**
 * @author Sergei Pomelov on 2.5.14 basic Algorithm component
 */
public abstract class AlgorithmComponent extends StructureElement {

    private static final long serialVersionUID = -7277409404088617966L;

    AlgorithmComponent(@Nonnull final String name) {
        super(name);
    }

    @Nonnull
    @Override
    public final ComponentType getComponentType() {
        return ComponentType.ALGORITHM;
    }
}

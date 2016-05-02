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

package simulation.structures.architecture;

import javax.annotation.Nonnull;

import simulation.structures.commons.ComponentType;
import simulation.structures.commons.StructureElement;

/**
 * A basic physical component.
 * @author Sergey Pomelov on 12/5/14.
 */
public abstract class ArchitectureComponent extends StructureElement {

    private static final long serialVersionUID = -4700793405282367855L;

    ArchitectureComponent(@Nonnull String name) {
        super(name);
    }

    @Nonnull
    @Override
    public final ComponentType getComponentType() {
        return ComponentType.ARCHITECTURE;
    }

    @Nonnull
    protected abstract ArchitectureComponentType getArchitectureComponentType();
}

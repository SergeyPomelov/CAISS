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

package simulation.structures.commons;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import simulation.structures.interaction.SerializableElement;

/**
 * @author Sergei Pomelov on 11.11.14. Main computational process controller
 */
public abstract class StructureElement implements SerializableElement, ComponentTyped {

    private static final long serialVersionUID = 6086798332605789768L;
    @Nonnull
    private String name = "";

    protected StructureElement(@Nonnull final String inName) {
        name = inName;
    }

    @Nonnull
    @Nonnegative
    @Override
    public final Long getId() {
        return 0L;
    }

    @Nonnull
    @Override
    public final String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String info() {
        return name;
    }

    @Override
    public String toString() {
        return info();
    }
}

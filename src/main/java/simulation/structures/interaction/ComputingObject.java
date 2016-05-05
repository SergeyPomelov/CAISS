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

package simulation.structures.interaction;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import util.Restrictions;

/**
 * Computational abstract actor.
 *
 * @author Sergey Pomelov on 2/5/14.
 */
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class ComputingObject implements SerializableElement {

    private static final long serialVersionUID = 6086798332605789768L;
    /** id for future DB */
    @Nonnull
    @Nonnegative
    private static final Long id = 0L;
    /** human-friendly name */
    @Nonnull
    private final String name;

    /** @param name ordinary human-friendly name of the object */
    ComputingObject(String name) {
        Restrictions.ifContainsNullFastFail(name);
        this.name = name;
    }

    @Nonnull
    @Nonnegative
    @Override
    public final Long getId() {
        return id;
    }

    @Nonnull
    @Override
    public final String getName() {
        return name;
    }

    /** @return human-friendly formatted information about the object */
    @Nonnull
    @Override
    public String info() {
        return name;
    }
}

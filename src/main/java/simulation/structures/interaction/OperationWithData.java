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


import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import util.Restrictions;

/**
 * @author Sergey Pomelov on 2/5/14.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class OperationWithData extends ComputingObject {

    private static final long serialVersionUID = 5318763246120708102L;
    @Nonnull
    private final OperationType type;                  // what we do
    @Nonnull
    private final DataBlock data;                      // with what

    public OperationWithData(OperationWithData toCopy) {
        this(toCopy.getName(), toCopy.type, toCopy.data);
    }

    /**
     * @param type which operation
     * @param data how fast we can do it
     */
    public OperationWithData(String name, OperationType type, DataBlock data) {
        super(name);
        Restrictions.ifContainsNullFastFail(type, data);
        this.type = type;
        this.data = data;
    }

    @Nonnull
    public OperationType getType() {
        return type;
    }

    @Nonnull
    public DataBlock getData() {
        return data;
    }

    @Nonnull
    @Override
    public String info() {
        return (String.format("%s-%s %s", super.info(), type, data.info()));
    }
}

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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import util.Restrictions;

/**
 * Data representation.
 * @author Sergey Pomelov on 2/5/14.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class DataBlock extends ComputingObject {

    private static final long serialVersionUID = 2834169680429419028L;
    /** type of contained data */
    @Nonnull
    private final DataType type;
    /** how many data objects of this type are contained here */
    @Nonnull
    @Nonnegative
    private final Long size;

    /** @param toCopy DataBlock for copying */
    DataBlock(DataBlock toCopy) {
        this(toCopy.getName(), toCopy.type, toCopy.size);
    }

    /**
     * @param name ordinary human-friendly name of the object
     * @param type type of contained data
     * @param size how many data objects of this type are contained here
     */
    public DataBlock(String name, DataType type, @Nonnegative Long size) {
        super(name);
        Restrictions.ifContainsNullFastFail(name, type);
        Restrictions.ifNotOnlyPositivesFastFail(size);
        this.type = type;
        this.size = size;
    }

    @Nonnull
    public DataType getType() {
        return type;
    }

    @Nonnull
    @Nonnegative
    public Long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DataBlock) && ((((DataBlock) obj).type == type)
                && (((DataBlock) obj).size.equals(size)));
    }

    @Override
    public int hashCode() {
        return type.ordinal() + size.intValue();
    }

    @Override
    @Nonnull
    public String info() {
        return String.format("%s {%s*%s}", super.info(), type.name(), size.toString());
    }
}

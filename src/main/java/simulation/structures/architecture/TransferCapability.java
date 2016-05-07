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

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataType;

import static util.Restrictions.ifNegativeFail;
import static util.Restrictions.ifNullFail;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
@Immutable
final class TransferCapability implements Serializable {

    private static final long serialVersionUID = -4768998459274666697L;

    @Nonnull
    private final DataType dataType;
    @Nonnegative
    private final int dataCapacity;
    @Nonnegative
    private final long dataTransferSpeed;

    TransferCapability(DataType dataType, @Nonnegative int dataCapacity,
                       @Nonnegative long dataTransferSpeed) {
        this.dataType = ifNullFail(dataType);
        this.dataCapacity = ifNegativeFail(dataCapacity);
        this.dataTransferSpeed = ifNegativeFail(dataTransferSpeed);
    }

    @Nonnull
    DataType getDataType() {
        return dataType;
    }

    @Nonnegative
    int getDataCapacity() {
        return dataCapacity;
    }

    @Nonnegative
    long getDataTransferSpeed() {
        return dataTransferSpeed;
    }
}

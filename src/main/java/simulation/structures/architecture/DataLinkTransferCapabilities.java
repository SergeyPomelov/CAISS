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

package simulation.structures.architecture;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataType;

import static util.ConversionUtil.nullFilter;

/**
 * @author Sergey Pomelov on 15/04/2016.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
final class DataLinkTransferCapabilities implements Serializable {

    private static final long serialVersionUID = -5931373465895118508L;

    @Nonnull
    private final List<DataType> dataType;
    @Nonnull
    private final List<Integer> dataCapacity;
    @Nonnull
    private final List<Long> dataTransferSpeed;
    @Nonnull
    @Nonnegative
    private final Long byteCapacity;

    DataLinkTransferCapabilities(DataLinkTransferCapabilities init) {
        this(init.dataType, init.dataCapacity, init.dataTransferSpeed, init.byteCapacity);
    }

    /**
     * @param dataType     what data types link could transfer
     * @param dataCapacity how much data we could transfer. Element number i means how much we can
     *                     capacity type of {code inDataType.get(i)}
     * @param byteCapacity how much physically data could be transferred
     */
    DataLinkTransferCapabilities(Collection<DataType> dataType, Collection<Integer> dataCapacity,
                                 Collection<Long> dataTransferSpeed, Long byteCapacity) {
        this.dataType = ImmutableList.copyOf(nullFilter(dataType));
        this.dataCapacity = ImmutableList.copyOf(nullFilter(dataCapacity));
        this.dataTransferSpeed = ImmutableList.copyOf(nullFilter(dataTransferSpeed));
        this.byteCapacity = byteCapacity;
    }

    @Nonnull
    public List<DataType> getDataType() {
        return dataType;
    }

    @Nonnull
    public List<Integer> getDataCapacity() {
        return dataCapacity;
    }

    @Nonnull
    public List<Long> getDataTransferSpeed() {
        return dataTransferSpeed;
    }

    @Nonnull
    public Long getByteCapacity() {
        return byteCapacity;
    }
}

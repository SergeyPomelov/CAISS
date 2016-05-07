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

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import static util.ConversionUtil.nullFilter;
import static util.Restrictions.ifNegativeFail;

/**
 * @author Sergey Pomelov on 15/04/2016.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
final class TransferCapabilities implements Serializable {

    private static final long serialVersionUID = -5931373465895118508L;

    @Nonnull
    private final List<TransferCapability> transferCapabilities;
    @Nonnull
    @Nonnegative
    private final Long byteCapacity;

    TransferCapabilities(TransferCapabilities toCopy) {
        this(toCopy.transferCapabilities, toCopy.byteCapacity);
    }

    /**
     * @param transferCapabilities what data types link could transfer and how much data we could
     *                             transfer.
     * @param byteCapacity         how much physically data could be transferred
     */
    TransferCapabilities(Collection<TransferCapability> transferCapabilities, Long byteCapacity) {
        this.transferCapabilities = ImmutableList.copyOf(nullFilter(transferCapabilities));
        this.byteCapacity = ifNegativeFail(byteCapacity);
    }

    @Nonnull
    List<TransferCapability> getTransferCapabilities() {
        return transferCapabilities;
    }

    @Nonnull
    Long getByteCapacity() {
        return byteCapacity;
    }
}

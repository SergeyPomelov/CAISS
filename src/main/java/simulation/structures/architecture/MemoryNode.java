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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;

import static util.ConversionUtil.nullFilter;
import static util.Restrictions.ifNegativeFail;

/**
 * Operation Memory.
 * @author Sergey Pomelov on 2/5/14.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
public final class MemoryNode extends ArchitectureComponent {

    private static final long serialVersionUID = 5900012936947627885L;

    @Nonnull
    @Nonnegative
    private final BigDecimal byteCapacity;
    @Nonnull
    private final List<DataBlock> dataCapacity;

    MemoryNode(MemoryNode toCopy) {
        this(toCopy.getName(), toCopy.dataCapacity, toCopy.byteCapacity);
    }

    MemoryNode(String name, Collection<DataBlock> dataCapacity,
               @Nonnegative BigDecimal byteCapacity) {
        super(name);
        this.dataCapacity = ImmutableList.copyOf(nullFilter(dataCapacity));
        this.byteCapacity = ifNegativeFail(byteCapacity);
    }

    @Nonnull
    @Nonnegative
    public BigDecimal getDataByteCapacity() {
        return byteCapacity;
    }

    @Nonnull
    @Override
    public ArchitectureComponentType getArchitectureComponentType() {
        return ArchitectureComponentType.MEMORY_NODE;
    }

    @Nonnull
    @Override
    public String info() {
        final StringBuilder output = new StringBuilder(32);
        output.append(String.format("%s %sb :", super.info(), byteCapacity));
        dataCapacity.forEach(entry -> output.append(String.format(" %s", entry.info())));
        return output.toString();
    }

    @Nonnull
    List<DataBlock> getDataCapacityList() {
        return dataCapacity;
    }

    @Nonnull
    @VisibleForTesting
    DataBlock getDataCapacity(int i) {
        if (i >= dataCapacity.size()) {
            throw new IllegalArgumentException("cap exceed");
        }
        return dataCapacity.get(i);
    }
}

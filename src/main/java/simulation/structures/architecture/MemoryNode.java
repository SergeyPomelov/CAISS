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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataType;

/**
 * @author Sergei Pomelov on 2.5.14. Operation Memory
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class MemoryNode extends ArchitectureComponent {

    private static final long serialVersionUID = 5900012936947627885L;
    @Nonnull
    @Nonnegative
    private final BigDecimal byteCapacity;
    @Nonnull
    private final List<DataType> dataType;
    @Nonnull
    private final List<Integer> dataCapacity;

    public MemoryNode(MemoryNode init) {
        this(init.getName(), init.dataType, init.dataCapacity, init.byteCapacity);
    }

    public MemoryNode(String inName, Collection<DataType> inDataType,
                      Collection<Integer> inDataCapacity, @Nonnegative BigDecimal inByteCapacity) {
        super(inName);
        dataType = ImmutableList.copyOf(inDataType);
        dataCapacity = ImmutableList.copyOf(inDataCapacity);
        byteCapacity = inByteCapacity;
    }

    @Nonnull
    public DataType getDataType(final int i) {
        if (i >= dataType.size()) {
            throw new IllegalArgumentException("cap exceed");
        }
        return dataType.get(i);
    }

    @Nonnull
    public List<DataType> getDataTypeList() {
        return dataType;
    }

    @Nonnull
    public List<Integer> getDataCapacityList() {
        return dataCapacity;
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
        if (dataCapacity.size() == dataType.size()) {
            dataType.forEach(dataTypeLocal -> output
                    .append(String.format(" %sX%s",
                            dataTypeLocal.name(),
                            getDataCapacity(dataType.indexOf(dataTypeLocal)).toString())));
        }
        return output.toString();
    }

    @Nonnull
    @Nonnegative
    private Integer getDataCapacity(final int i) {
        if (i >= dataType.size()) {
            throw new IllegalArgumentException("cap exceed");
        }
        return dataCapacity.get(i);
    }
}

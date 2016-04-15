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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;
import static util.ConversionUtil.nullFilter;

/**
 * @author Sergei Pomelov on 2.5.14. Link between components
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class DataLink extends ArchitectureComponent {

    private static final long serialVersionUID = -4090489770450876487L;
    private static final String LINK_CAP_OVERWHELMED = "Link cap overwhelmed";

    @Nonnull
    private final DataLinkTransferCapabilities dataLinkTransferCapabilities;
    @Nonnull
    private final List<ArchitectureComponent> in;
    @Nonnull
    private final List<ArchitectureComponent> out;

    public DataLink(DataLink init) {
        this(init.getName(), init.dataLinkTransferCapabilities, init.in, init.out);
    }

    /**
     * @param inName name of physical link
     * @param inIn   from where link can transfer
     * @param inOut  to where link can transfer
     */
    public DataLink(String inName,
                    DataLinkTransferCapabilities dataLinkTransferCapabilities,
                    Collection<ArchitectureComponent> inIn,
                    Collection<ArchitectureComponent> inOut) {
        super(inName);
        this.dataLinkTransferCapabilities = dataLinkTransferCapabilities;
        in = ImmutableList.copyOf(nullFilter(inIn));
        out = ImmutableList.copyOf(nullFilter(inOut));
    }

    @Nullable
    public Long getTransferTime(OperationWithData operationWithData) {
        if ((operationWithData.getType() == OperationType.TRANSFER)
                && getDataType().contains(operationWithData.getData().getType())) {
            final int i = getDataType().indexOf(operationWithData.getData().getType());
            return (long) (getDataTransferSpeed(i) *
                    ((float) operationWithData.getData().getSize() / getDataCapacity(i)));
        }
        return null;
    }

    @Nonnull
    public List<ArchitectureComponent> getIn() {
        return in;
    }

    @Nonnull
    public ArchitectureComponent getIn(int i) {
        if (i >= in.size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return in.get(i);
    }

    @Nonnull
    public List<ArchitectureComponent> getOut() {
        return out;
    }

    @Nonnull
    public ArchitectureComponent getOut(int i) {
        if (i >= out.size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return out.get(i);
    }

    @Nullable
    public Collection<ArithmeticNode> getArithmeticNodes() {
        final Collection<ArithmeticNode> cores = new ArrayList<>(5);
        final Collection<ArchitectureComponent> all = new ArrayList<>(in);
        all.addAll(out);
        all.stream().filter(el ->
                el.getArchitectureComponentType() == ArchitectureComponentType.ARITHMETIC_NODE)
                .forEach(el -> {
                    final ArithmeticNode node = (ArithmeticNode) el;
                    cores.add(node);
                });
        return ImmutableList.copyOf(cores);
    }

    @Nullable
    public Collection<MemoryNode> getMemoryNodes() {
        final Collection<MemoryNode> memory = new ArrayList<>(1);
        final Collection<ArchitectureComponent> all = new ArrayList<>(in);
        all.addAll(out);

        all.stream().filter(el ->
                el.getArchitectureComponentType() == ArchitectureComponentType.MEMORY_NODE)
                .forEach(el -> {
                    final MemoryNode node = (MemoryNode) el;
                    memory.add(node);
                });
        return ImmutableList.copyOf(memory);
    }

    @Nonnull
    @Override
    public ArchitectureComponentType getArchitectureComponentType() {
        return ArchitectureComponentType.LINK;
    }

    @Nonnull
    @Override
    public String info() {
        final StringBuilder output = new StringBuilder(100);
        output.append(LS).append(LS).append("Link - ").append(super.info()).append(' ')
                .append(getDataByteCapacity())
                .append("b: ");

        for (final Long speed : getDataTransferSpeed()) {
            final int i = getDataTransferSpeed().indexOf(speed);
            output.append(String.format("| %s*%s - %smc ", getDataType(i).name(),
                    getDataCapacity(i).toString(), speed));
        }
        output.append(LS);

        output.append(LS).append(" In: ").append(LS);
        for (final ArchitectureComponent el : in) {
            output.append(String.format("| %s ", el.info()));
        }
        output.append(LS);

        output.append(LS).append(" Out:").append(LS);
        for (final ArchitectureComponent el : out) {
            output.append(String.format("| %s ", el.info()));
        }
        return output.toString();
    }

    @Nonnull
    private List<DataType> getDataType() {
        return dataLinkTransferCapabilities.getDataType();
    }

    @Nonnull
    private List<Integer> getDataCapacity() {
        return dataLinkTransferCapabilities.getDataCapacity();
    }

    @Nonnull
    @Nonnegative
    private Integer getDataCapacity(int i) {
        if (i >= getDataCapacity().size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return getDataCapacity().get(i);
    }

    @Nonnull
    private List<Long> getDataTransferSpeed() {
        return dataLinkTransferCapabilities.getDataTransferSpeed();
    }

    @Nonnull
    private Long getDataTransferSpeed(int i) {
        if (i > getDataTransferSpeed().size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return getDataTransferSpeed().get(i);
    }

    @Nonnull
    private Long getDataByteCapacity() {
        return dataLinkTransferCapabilities.getByteCapacity();
    }

    @Nonnull
    private DataType getDataType(int i) {
        if (i >= getDataType().size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return getDataType().get(i);
    }

}

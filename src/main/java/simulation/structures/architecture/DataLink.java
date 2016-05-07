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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;
import static util.ConversionUtil.nullFilter;
import static util.Restrictions.ifNullFail;

/**
 * Link between components.
 *
 * @author Sergey Pomelov on 2/5/14.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class DataLink extends ArchitectureComponent {

    private static final long serialVersionUID = -4090489770450876487L;
    private static final String ILLEGAL_IDX = "Illegal idx.";

    @Nonnull
    private final TransferCapabilities transferCapabilities;
    @Nonnull
    private final List<ArchitectureComponent> in;
    @Nonnull
    private final List<ArchitectureComponent> out;

    public DataLink(DataLink toCopy) {
        this(toCopy.getName(), toCopy.transferCapabilities, toCopy.in, toCopy.out);
    }

    /**
     * @param name name of physical link
     * @param in   from where link can transfer
     * @param out  to where link can transfer
     */
    DataLink(String name, TransferCapabilities transferCapabilities,
             Collection<ArchitectureComponent> in, Collection<ArchitectureComponent> out) {
        super(name);
        this.transferCapabilities = ifNullFail(transferCapabilities);
        this.in = ImmutableList.copyOf(nullFilter(in));
        this.out = ImmutableList.copyOf(nullFilter(out));
    }

    @Nonnull
    public Optional<Float> getTransferTime(OperationWithData operationWithData) {
        final Optional<TransferCapability> capability = findTransferCapability(operationWithData);
        if (capability.isPresent()) {
            return Optional.of((capability.get().getDataTransferSpeed() *
                    ((float) operationWithData.getData().getSize()
                            / capability.get().getDataCapacity())));
        }
        return Optional.empty();
    }

    @Nonnull
    public List<ArchitectureComponent> getIn() {
        return in;
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

        for (final TransferCapability capability : getTransferCapabilities()) {
            output.append(String.format("| %s*%s - %smc ", capability.getDataType().name(),
                    capability.getDataCapacity(), capability.getDataTransferSpeed()));
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

    private Optional<TransferCapability> findTransferCapability(OperationWithData operation) {
        if ((operation.getType() == OperationType.TRANSFER)) {
            for (TransferCapability capability : getTransferCapabilities()) {
                if (capability.getDataType() == operation.getData().getType()) {
                    return Optional.of(capability);
                }
            }
        }
        return Optional.empty();
    }

    @Nonnull
    ArchitectureComponent getIn(int i) {
        if ((i < 0) || (i >= in.size())) {
            throw new IllegalArgumentException(ILLEGAL_IDX);
        }
        return in.get(i);
    }

    @Nonnull
    List<ArchitectureComponent> getOut() {
        return out;
    }

    @Nonnull
    ArchitectureComponent getOut(int i) {
        if ((i < 0) || (i >= out.size())) {
            throw new IllegalArgumentException(ILLEGAL_IDX);
        }
        return out.get(i);
    }

    @Nonnull
    Collection<CalculationNode> getCalculationNodes() {
        final Collection<CalculationNode> calculationNodes = new ArrayList<>(5);
        final Collection<ArchitectureComponent> all = new ArrayList<>(in);
        all.addAll(out);
        all.stream().filter(el ->
                el.getArchitectureComponentType() == ArchitectureComponentType.CALCULATION_NODE)
                .forEach(el -> {
                    final CalculationNode node = (CalculationNode) el;
                    calculationNodes.add(node);
                });
        return ImmutableList.copyOf(calculationNodes);
    }

    @Nonnull
    Collection<MemoryNode> getMemoryNodes() {
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

    @Nonnegative
    private int getDataCapacity(int i) {
        if (i >= transferCapabilities.getTransferCapabilities().size()) {
            throw new IllegalArgumentException(ILLEGAL_IDX);
        }
        return getTransferCapabilities().get(i).getDataCapacity();
    }


    private long getDataTransferSpeed(int i) {
        if (i > getTransferCapabilities().size()) {
            throw new IllegalArgumentException(ILLEGAL_IDX);
        }
        return getTransferCapabilities().get(i).getDataTransferSpeed();
    }

    private List<TransferCapability> getTransferCapabilities() {
        return transferCapabilities.getTransferCapabilities();
    }


    @Nonnull
    private Long getDataByteCapacity() {
        return transferCapabilities.getByteCapacity();
    }

    @Nonnull
    private DataType getDataType(int i) {
        if (i >= getTransferCapabilities().size()) {
            throw new IllegalArgumentException(ILLEGAL_IDX);
        }
        return getTransferCapabilities().get(i).getDataType();
    }

}

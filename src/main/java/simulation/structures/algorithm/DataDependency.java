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

package simulation.structures.algorithm;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;
import static util.ConversionUtil.nullFilter;
import static util.Restrictions.ifNullFail;

/**
 * Dependencies on algorithm steps a.k.a. data flow dependencies
 * @author Sergey Pomelov on 2/10/14.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class DataDependency extends AlgorithmComponent {

    private static final long serialVersionUID = 988170536445679820L;
    private static final String OVERWHELMED = "Link cap overwhelmed";
    /** flow operation type */
    @Nonnull
    private final DependencyType type;
    /** operations at this point */
    @Nonnull
    private final List<OperationWithData> operations;
    /** next flow point */
    private final List<DataDependency> next;

    DataDependency(DataDependency toCopy) {
        this(toCopy.getName(), toCopy.type,
                toCopy.operations, toCopy.next);
    }

    /**
     * @param name name of transfer operation
     * @param type flow operation type
     * @param operations operations at this point
     * @param next next flow point
     */
    DataDependency(String name, DependencyType type, Collection<OperationWithData> operations,
                   Collection<DataDependency> next) {
        super(name);
        this.type = ifNullFail(type);
        this.operations = ImmutableList.copyOf(nullFilter(operations));
        this.next = ImmutableList.copyOf(nullFilter(next));
        checkNext();
    }

    private void checkNext() {
        if (next.contains(this)) {
            throw new IllegalStateException("Dependency can't point itself as next flow point.");
        }
    }

    @Nonnull
    public OperationWithData getOperation(int i) {
        if (i >= operations.size()) {
            throw new IllegalArgumentException(OVERWHELMED);
        }
        return operations.get(i);
    }

    @Nonnull
    public Collection<DataBlock> getUsedData() {
        final Collection<DataBlock> usedData = new ArrayList<>(10);
        final Iterable<OperationWithData> all = new ArrayList<>(operations);
        all.forEach(el -> usedData.add(el.getData()));
        return ImmutableList.copyOf(usedData);
    }

    @Nonnull
    @Override
    public String info() {
        final StringBuilder output = new StringBuilder(128);
        return printNode(output, "");
    }

    private String innerData() {
        final StringBuilder output = new StringBuilder(128);
        for (final OperationWithData el : operations) {
            output.append(String.format(" | %s", el.info()));
        }
        return output.toString();
    }

    private String printNode(StringBuilder output, String prefix) {

        final boolean isTail = next.isEmpty();
        output.append(prefix).append(isTail ? "└── " : "├── ").append(innerData()).append(LS);
        for (int i = 0; i < (next.size() - 1); i++) {
            next.get(i).printNode(output, prefix + (isTail ? "    " : "│     "));
        }
        if (!isTail) {
            next.get(next.size() - 1).printNode(output, prefix + "     ");
        }
        return output.toString();
    }

    @Nonnull
    public Iterable<OperationWithData> getOperations() {
        return operations;
    }

    @Nonnull
    public Collection<DataDependency> getNext() {
        return next;
    }

    @Nonnull
    DependencyType getType() {
        return type;
    }

}

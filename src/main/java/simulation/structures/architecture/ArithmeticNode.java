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

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.OperationPerformance;
import simulation.structures.interaction.OperationWithData;

/**
 * @author Sergei Pomelov on 2.5.14. Computation node or core
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class ArithmeticNode extends ArchitectureComponent {

    private static final long serialVersionUID = -2778594112555207896L;
    @Nonnull
    private final List<OperationPerformance> allowedOperations;

    public ArithmeticNode(ArithmeticNode init) {
        this(init.getName(), init.allowedOperations);
    }

    public ArithmeticNode(String inName, Collection<OperationPerformance> inAllowedOperations) {
        super(inName);
        allowedOperations = ImmutableList.copyOf(inAllowedOperations);
    }

    @Nullable
    public Long getOperationTime(OperationWithData operationWithData) {
        for (final OperationPerformance oper : allowedOperations) {
            if (oper.getOperation().getType() == operationWithData.getType()) {
                final int i = allowedOperations.indexOf(oper);
                return (long) (getAllowedOperation(i).getTime() *
                        ((float) operationWithData.getData().getSize()
                                / getAllowedOperation(i).getOperation().getData().getSize()
                        ));
            }
        }
        return null;
    }

    @Nonnull
    public Collection<OperationPerformance> getAllowedOperations() {
        return allowedOperations;
    }

    @Nonnull
    @Override
    public ArchitectureComponentType getArchitectureComponentType() {
        return ArchitectureComponentType.ARITHMETIC_NODE;
    }

    @Nonnull
    @Override
    public String info() {
        final StringBuilder output = new StringBuilder(50);
        output.append(String.format("%s :", super.info()));
        for (final OperationPerformance operationPerformance : allowedOperations) {
            output.append(String.format(" %s;", operationPerformance.info()));
        }
        return output.toString();
    }

    @Nonnull
    private OperationPerformance getAllowedOperation(int i) {
        if (i > allowedOperations.size()) {
            throw new IllegalArgumentException("cap overwhelmed");
        }
        return allowedOperations.get(i);
    }
}

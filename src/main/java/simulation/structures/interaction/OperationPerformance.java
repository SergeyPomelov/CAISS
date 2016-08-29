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


import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import static util.Restrictions.ifNegativeFail;
import static util.Restrictions.ifNullFail;

/**
 * Representation of how fast we can do this {@code OperationWithData.class}.
 *
 * @author Sergey Pomelov on 2/5/14.
 * @see OperationWithData
 */
@Immutable
@ParametersAreNonnullByDefault
public final class OperationPerformance extends ComputingObject {

    private static final long serialVersionUID = -5609898011539390298L;

    @Nonnull
    private final OperationWithData operation;                  // what we do
    private final ResourceComplexity complexity;                // how fast

    OperationPerformance(OperationPerformance operation) {
        this(operation.getName(), operation.operation, operation.complexity);
    }

    public OperationPerformance(String name, OperationWithData operation, long resources) {
        this(name, operation, size -> Math.round((size / (float) operation.getData().getSize()) * resources));
        ifNegativeFail(resources);
    }

    /**
     * @param operation which operation
     * @param complexity how mach resources we need for various data size. A function.
     */
    public OperationPerformance(String name, OperationWithData operation, ResourceComplexity complexity) {
        super(name);
        this.operation = ifNullFail(operation);
        this.complexity = ifNullFail(complexity);
    }

    @Nonnull
    public OperationWithData getOperation() {
        return operation;
    }

    public long getNeededResources(long problemSize) {
        return complexity.getComplexity(problemSize);
    }

    @Nonnull
    @Override
    public String info() {
        return (String.format("%s(%s) t=%s", super.info(), operation.info(), complexity));
    }
}

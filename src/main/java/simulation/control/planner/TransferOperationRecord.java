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

package simulation.control.planner;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.architecture.CalculationNode;
import simulation.structures.architecture.DataLink;
import simulation.structures.architecture.MemoryNode;
import simulation.structures.interaction.OperationWithData;

/**
 * @author Sergey Pomelov on 30/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
final class TransferOperationRecord extends OperationRecord {
    private final MemoryNode memory;
    private final DataLink dataLink;
    private final CalculationNode core;
    private final OperationWithData operationWithData;

    TransferOperationRecord(long start, long end, @Nullable TimeUnit timeUnit,
                            MemoryNode memory, DataLink dataLink, CalculationNode core,
                            OperationWithData operationWithData) {
        super(start, end, timeUnit);
        this.memory = memory;
        this.dataLink = dataLink;
        this.core = core;
        this.operationWithData = operationWithData;
    }

    @Nonnull
    @Override
    public String toString() {
        return "transfer: " + memory.getName() + "=>" + dataLink.getName() + "=>" + core.getName() + " : " +
                operationWithData.getData().info() + ' ' + " : " + operationWithData.info()
                + ' ' + super.toString();
    }
}

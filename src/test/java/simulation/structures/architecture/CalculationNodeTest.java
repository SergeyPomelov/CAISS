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

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import simulation.structures.commons.ComponentType;
import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationPerformance;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 05/05/2016.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "ConstantConditions"})
public class CalculationNodeTest {

    private static final String CORE_PERFORMANCE = "core performance";
    private static final String WHOLE_ARRAY = "wholeArray";
    private static final String INVERSE = "inverse";
    private static final long MATRIX_SIZE = 1000L * 1000L;
    private static final long SUB_MATRIX_SIZE = 500L * 500L;

    private static final MemoryNode MEMORY = new MemoryNode("DDR3",
            ImmutableList.of(new DataBlock("8bytes", DataType.EIGHT_B_FL, MATRIX_SIZE)),
            BigDecimal.valueOf(8L * MATRIX_SIZE));

    private static final DataBlock MATRIX =
            new DataBlock(WHOLE_ARRAY, DataType.FOUR_B_FL, MATRIX_SIZE);
    private static final DataBlock SUB_MATRIX =
            new DataBlock("subArray", DataType.FOUR_B_FL, SUB_MATRIX_SIZE);

    private static final OperationWithData INVERSE_LARGE =
            new OperationWithData(INVERSE, OperationType.INVERSE, MATRIX);
    private static final OperationWithData INVERSE_SMALL =
            new OperationWithData(INVERSE, OperationType.INVERSE, SUB_MATRIX);

    private static final OperationPerformance INVERSE_LARGE_PERFORMANCE =
            new OperationPerformance(CORE_PERFORMANCE, INVERSE_LARGE, 4000L);
    private static final OperationPerformance INVERSE_SMALL_PERFORMANCE =
            new OperationPerformance(CORE_PERFORMANCE, INVERSE_SMALL, 2000L);
    private static final Collection<OperationPerformance> performances = ImmutableList.of(
            INVERSE_LARGE_PERFORMANCE, INVERSE_SMALL_PERFORMANCE);

    static final CalculationNode CORE = new CalculationNode("Core1", performances);


    @Test
    public void nullPhysicalCapacity() {
        passedIfException(() -> new CalculationNode("Core1", null));
    }

    @Test
    public void copy() {
        final CalculationNode core2 = new CalculationNode(CORE);
        assertNotSame(CORE, core2);
        assertEquals(CORE.getAllowedOperations(), core2.getAllowedOperations());
    }

    @Test
    public void getArchitectureComponentType() {
        assertEquals(ArchitectureComponentType.CALCULATION_NODE,
                CORE.getArchitectureComponentType());
    }

    @Test
    public void getComponentType() {
        assertEquals(ComponentType.ARCHITECTURE, CORE.getComponentType());
    }

    @Test
    public void info() {
        assertNotNull(CORE.info());
    }

    @Test
    public void getOperationTime() {
        assertEquals(INVERSE_LARGE_PERFORMANCE.getTime() / (MATRIX_SIZE / SUB_MATRIX_SIZE),
                CORE.getOperationTime(INVERSE_SMALL).orElse(0L).longValue());
    }

    @Test
    public void getOperationTimeOneOperation() {
        final Collection<OperationPerformance> performances2 = ImmutableList.of(
                INVERSE_SMALL_PERFORMANCE);
        final CalculationNode core2 = new CalculationNode("Core1", performances2);
        assertEquals(INVERSE_SMALL_PERFORMANCE.getTime(),
                core2.getOperationTime(INVERSE_SMALL).orElse(0L).longValue());
    }

    @Test
    public void getOperationTimeFromNonExistentOperation() {
        final OperationWithData transfer =
                new OperationWithData("deadBeef", OperationType.TRANSFER, SUB_MATRIX);
        assertFalse(CORE.getOperationTime(transfer).isPresent());
    }

    @Test
    public void getAllowedOperations() {
        assertEquals(performances, CORE.getAllowedOperations());
    }
}

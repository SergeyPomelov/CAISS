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

import java.util.List;
import java.util.Optional;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static simulation.structures.architecture.CalculationNodeTest.CORE;
import static simulation.structures.architecture.MemoryNodeTest.MEMORY;
import static simulation.structures.architecture.TransferCapabilitiesTest.TRANSFER_CAPABILITIES;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class DataLinkTest {

    private static final String NON_TRANSFER = "nonTransfer";

    private static final CalculationNode CORE_1 = new CalculationNode(CORE);
    private static final CalculationNode CORE_2 = new CalculationNode(CORE);
    private static final List<ArchitectureComponent> IN = ImmutableList.of(MEMORY);
    private static final List<ArchitectureComponent> OUT = ImmutableList.of(CORE_1, CORE_2);

    static final DataLink LINK = new DataLink("bus", TRANSFER_CAPABILITIES, IN, OUT);

    @Test
    public void nullCapabilities() {
        passedIfException(() -> new DataLink("bus", null, IN, OUT));
    }

    @Test
    public void nullIn() {
        passedIfException(() -> new DataLink("bus", TRANSFER_CAPABILITIES, null, OUT));
    }

    @Test
    public void nullOut() {
        passedIfException(() -> new DataLink("bus", TRANSFER_CAPABILITIES, IN, null));
    }

    @Test
    public void getIn() {
        assertEquals(IN, LINK.getIn());
    }

    @Test
    public void getInIDX() {
        assertEquals(IN.get(0), LINK.getIn(0));
    }

    @Test
    public void getInIdxOverwhelmed() {
        passedIfException(() -> LINK.getIn(IN.size()));
    }

    @Test
    public void getInIdxNegative() {
        passedIfException(() -> LINK.getIn(-1));
    }

    @Test
    public void getOut() {
        assertEquals(OUT, LINK.getOut());
    }

    @Test
    public void getOutIdx() {
        assertEquals(OUT.get(0), LINK.getOut(0));
        assertEquals(OUT.get(1), LINK.getOut(1));
    }

    @Test
    public void getOutIdxOverwhelmed() {
        passedIfException(() -> LINK.getOut(OUT.size()));
    }

    @Test
    public void getOutIdxNegative() {
        passedIfException(() -> LINK.getOut(-1));
    }

    @Test
    public void getArchitectureComponentType() {
        assertEquals(ArchitectureComponentType.LINK, LINK.getArchitectureComponentType());
    }

    @Test
    public void info() {
        assertNotNull(LINK.info());
    }

    @Test
    public void getTransferTime() {
        final OperationWithData oper =
                new OperationWithData(NON_TRANSFER, OperationType.TRANSFER,
                        new DataBlock("data", TRANSFER_CAPABILITIES
                                .getTransferCapabilities().get(0).getDataType(), 1000L));
        assertEquals(Optional.of(1.0F), LINK.getTransferTime(oper));
    }

    @Test
    public void getTransferTimeOnNotTransfer() {
        final OperationWithData oper =
                new OperationWithData(NON_TRANSFER, OperationType.ADDITION,
                        new DataBlock("data", DataType.BOOL, 1L));
        assertEquals(Optional.empty(), LINK.getTransferTime(oper));
    }

    @Test
    public void getTransferTimeNotSupported() {
        final OperationWithData oper =
                new OperationWithData(NON_TRANSFER, OperationType.TRANSFER,
                        new DataBlock("data", DataType.BOOL, 1L));
        assertEquals(Optional.empty(), LINK.getTransferTime(oper));
    }

    @Test
    public void getArithmeticNodes() {
        assertEquals(ImmutableList.of(CORE_1, CORE_2), LINK.getCalculationNodes());
    }


    @Test
    public void getMemoryNodes() {
        assertEquals(ImmutableList.of(MEMORY), LINK.getMemoryNodes());
    }

}

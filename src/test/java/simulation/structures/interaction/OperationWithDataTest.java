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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 05/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class OperationWithDataTest {

    private static final String NAME = "operWithData1";
    private static final OperationType OPER_TYPE = OperationType.ADDITION;
    private static final DataBlock DATA_BLOCK_1 = new DataBlock("dataBlock1", DataType.B_DEC, 1L);
    private static final OperationWithData OPERATION_WITH_DATA =
            new OperationWithData(NAME, OPER_TYPE, DATA_BLOCK_1);

    @Test
    public void nullName() {
        passedIfException(() -> new OperationWithData(null, OPER_TYPE, DATA_BLOCK_1));
    }

    @Test
    public void nullType() {
        passedIfException(() -> new OperationWithData(NAME, null, DATA_BLOCK_1));
    }

    @Test
    public void nullDataBlock() {
        passedIfException(() -> new OperationWithData(NAME, OPER_TYPE, null));
    }

    @Test
    public void notReferenceCopy() {
        final OperationWithData operationWithData2 = new OperationWithData(OPERATION_WITH_DATA);
        assertNotSame(OPERATION_WITH_DATA, operationWithData2);
    }

    @Test
    public void getName() {
        assertEquals(NAME, OPERATION_WITH_DATA.getName());
    }

    @Test
    public void getType() {
        assertEquals(OPER_TYPE, OPERATION_WITH_DATA.getType());
    }

    @Test
    public void getData() {
        assertEquals(DATA_BLOCK_1, OPERATION_WITH_DATA.getData());
    }

    @Test
    public void info() {
        assertNotNull(OPERATION_WITH_DATA.info());
    }
}

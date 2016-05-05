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

    private static final String name = "operWithData1";
    private static final OperationType operType = OperationType.ADDITION;
    private static final DataBlock dataBlock1 = new DataBlock("dataBlock1", DataType.B_DEC, 1L);
    private static final OperationWithData operationWithData1 =
            new OperationWithData(name, operType, dataBlock1);

    @Test
    public void nullName() {
        passedIfException(() -> new OperationWithData(null, operType, dataBlock1));
    }

    @Test
    public void nullType() {
        passedIfException(() -> new OperationWithData(name, null, dataBlock1));
    }

    @Test
    public void nullDataBlock() {
        passedIfException(() -> new OperationWithData(name, operType, null));
    }

    @Test
    public void notReferenceCopy() {
        final OperationWithData operationWithData2 = new OperationWithData(operationWithData1);
        assertNotSame(operationWithData1, operationWithData2);
    }

    @Test
    public void getName() {
        assertEquals(name, operationWithData1.getName());
    }

    @Test
    public void getType() {
        assertEquals(operType, operationWithData1.getType());
    }

    @Test
    public void getData() {
        assertEquals(dataBlock1, operationWithData1.getData());
    }

    @Test
    public void info() {
        assertNotNull(operationWithData1.info());
    }
}

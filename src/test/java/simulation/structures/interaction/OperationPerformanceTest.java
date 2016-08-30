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
import static org.junit.Assert.assertSame;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 05/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class OperationPerformanceTest {

    private static final long SIZE = 1L;
    private static final DataBlock DATA = new DataBlock("data1", DataType.B_DEC, SIZE);
    private static final OperationWithData OPERATION =
            new OperationWithData("operationWithData1", OperationType.ADDITION, DATA);
    private static final OperationPerformance PERFORMANCE
            = new OperationPerformance("operationPerformance1", OPERATION, SIZE);

    @Test
    public void nullOperation() {
        passedIfException(() ->
                new OperationPerformance("operationPerformance1", null, SIZE));
    }

    @Test
    public void negativeSpeed() {
        passedIfException(() ->
                new OperationPerformance("operationPerformance1", OPERATION, -1L));
    }

    @Test
    public void notReferenceCopy() {
        final OperationPerformance operationPerformance2
                = new OperationPerformance(PERFORMANCE);
        assertNotSame(PERFORMANCE, operationPerformance2);
    }

    @Test
    public void getOperation() {
        assertSame(OPERATION, PERFORMANCE.getOperation());
    }

    @Test
    public void getTime() {
        assertEquals(SIZE, PERFORMANCE.getNeededResources(SIZE));
    }

    @Test
    public void info() {
        assertNotNull(OPERATION.info());
    }
}

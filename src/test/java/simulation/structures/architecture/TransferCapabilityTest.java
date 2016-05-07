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

import org.junit.Test;

import simulation.structures.interaction.DataType;

import static org.junit.Assert.assertEquals;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class TransferCapabilityTest {

    private static final DataType DATA_TYPE = DataType.FOUR_B_FL;
    private static final int CAPACITY = 1_000_000;
    private static final long SPEED = 1000L;

    private static final TransferCapability CAPABILITY =
            new TransferCapability(DATA_TYPE, CAPACITY, SPEED);

    @Test
    public void nullDataType() {
        passedIfException(() -> new TransferCapability(null, CAPACITY, SPEED));
    }

    @Test
    public void negativeDataCapacity() {
        passedIfException(() -> new TransferCapability(DATA_TYPE, -1, SPEED));
    }

    @Test
    public void negativeTransferSpeed() {
        passedIfException(() -> new TransferCapability(DATA_TYPE, CAPACITY, -1L));
    }

    @Test
    public void getDataType() {
        assertEquals(DATA_TYPE, CAPABILITY.getDataType());
    }

    @Test
    public void getDataCapacity() {
        assertEquals(CAPACITY, CAPABILITY.getDataCapacity());
    }

    @Test
    public void getDataTransferSpeed() {
        assertEquals(SPEED, CAPABILITY.getDataTransferSpeed());
    }
}

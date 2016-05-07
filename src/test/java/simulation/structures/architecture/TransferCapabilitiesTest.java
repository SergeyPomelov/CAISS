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

import simulation.structures.interaction.DataType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 06/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class TransferCapabilitiesTest {

    private static final DataType DATA_TYPE = DataType.FOUR_B_FL;
    private static final int CAPACITY = 1_000_000;
    private static final long SPEED = 1000L;
    private static final long BYTE_CAPACITY = 4000L;

    private static final List<TransferCapability> CAPABILITIES = ImmutableList.of(
            new TransferCapability(DATA_TYPE, CAPACITY, SPEED));

    static final TransferCapabilities TRANSFER_CAPABILITIES =
            new TransferCapabilities(CAPABILITIES, BYTE_CAPACITY);

    @Test
    public void nullDataTypes() {
        passedIfException(() -> new TransferCapabilities(null, BYTE_CAPACITY));
    }

    @Test
    public void nullByteCapacity() {
        passedIfException(() -> new TransferCapabilities(CAPABILITIES, null));
    }

    @Test
    public void negativeByteCapacity() {
        passedIfException(() -> new TransferCapabilities(CAPABILITIES, -1L));
    }

    @Test
    public void copy() {
        final TransferCapabilities capabilities1 = new TransferCapabilities(TRANSFER_CAPABILITIES);
        assertNotSame(TRANSFER_CAPABILITIES, capabilities1);
        assertEquals(TRANSFER_CAPABILITIES.getByteCapacity(), capabilities1.getByteCapacity());
        assertEquals(CAPABILITIES, TRANSFER_CAPABILITIES.getTransferCapabilities());
    }

    @Test
    public void getDataTransferSpeeds() {
        assertEquals(CAPABILITIES, TRANSFER_CAPABILITIES.getTransferCapabilities());
    }

    @Test
    public void getByteCapacities() {
        assertEquals(BYTE_CAPACITY, TRANSFER_CAPABILITIES.getByteCapacity().longValue());
    }

}

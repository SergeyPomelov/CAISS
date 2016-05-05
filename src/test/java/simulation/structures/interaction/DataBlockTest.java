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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static testutil.TestUtil.passedIfException;

/**
 * DataBlock Tester.
 *
 * @author Sergey Pomelov on 12.03.15.
 */
@SuppressWarnings("ConstantConditions")
public class DataBlockTest {

    private static final DataBlock data1 = new DataBlock("1", DataType.B_DEC, 2L);

    @Test
    public void nullName() {
        passedIfException(() -> new DataBlock(null, DataType.B_DEC, 1L));
    }

    @Test
    public void nullType() {
        passedIfException(() -> new DataBlock("1", null, 1L));
    }

    @Test
    public void negativeSize() {
        passedIfException(() -> new DataBlock("1", DataType.B_DEC, -1L));
    }

    @Test
    public void equals() {
        final DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    public void equalsDespiteName() {
        final DataBlock data2 = new DataBlock("2", DataType.B_DEC, 2L);
        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    public void notEquals() {
        final DataBlock data2 = new DataBlock("1", DataType.B_DEC, 1L);
        assertNotEquals(data1, data2);
        assertNotEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    public void equalsBack() {
        final DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(data2, data1);
    }

    @Test
    public void equalsReflective() {
        assertEquals(data1, data1);
    }

    @Test
    public void equalsCopy() {
        final DataBlock data2 = new DataBlock(data1);
        assertEquals(data1, data2);
    }

    @Test
    public void notReferenceCopy() {
        final DataBlock data2 = new DataBlock(data1);
        assertNotSame(data1, data2);
    }

    @Test
    public void getType() {
        final DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(DataType.B_DEC, data2.getType());
    }

    @Test
    public void getSize() {
        final DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(2L, data2.getSize().longValue());
    }

    @Test
    public void info() {
        assertNotNull(data1.info());
    }

    @Test
    public void getId() {
        assertNotNull(data1.getId());
    }
}

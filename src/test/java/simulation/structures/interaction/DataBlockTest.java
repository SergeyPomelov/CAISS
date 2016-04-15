/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * DataBlock Tester.
 *
 * @author created by Sergei Pomelov on 12.03.15.
 */
public class DataBlockTest {

    private final DataBlock data1 = new DataBlock("1", DataType.B_DEC, 2L);

    @Test
    public void testBadSize() {
        boolean thrown = false;
        try {
            new DataBlock("1", DataType.B_DEC, -1L);
        } catch (IllegalArgumentException ignored) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testEquals() {
        DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(data1, data2);
    }

    @Test
    public void testEqualsBack() {
        DataBlock data2 = new DataBlock("1", DataType.B_DEC, 2L);
        assertEquals(data2, data1);
    }

    @Test
    public void testEqualsReflective() {
        assertEquals(data1, data1);
    }

    @Test
    public void testEqualsCopy() {
        DataBlock data2 = new DataBlock(data1);
        assertEquals(data1, data2);
    }

    @Test
    public void testNotReferenceCopy() {
        DataBlock data2 = new DataBlock(data1);
        assertNotSame(data1, data2);
    }
}

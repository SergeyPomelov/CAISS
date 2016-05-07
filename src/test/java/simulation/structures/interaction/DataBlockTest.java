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
@SuppressWarnings({"ConstantConditions", "NonBooleanMethodNameMayNotStartWithQuestion"})
public class DataBlockTest {

    private static final String NAME = "data";
    private static final DataType TYPE = DataType.B_DEC;
    private static final long SIZE = 2L;
    private static final DataBlock DATA = new DataBlock(NAME, TYPE, SIZE);

    @Test
    public void nullName() {
        passedIfException(() -> new DataBlock(null, TYPE, SIZE));
    }

    @Test
    public void nullType() {
        passedIfException(() -> new DataBlock(NAME, null, SIZE));
    }

    @Test
    public void negativeSize() {
        passedIfException(() -> new DataBlock(NAME, TYPE, -1L));
    }

    @Test
    public void equals() {
        final DataBlock data2 = new DataBlock(NAME, TYPE, SIZE);
        assertEquals(DATA, data2);
        assertEquals(DATA.hashCode(), data2.hashCode());
    }

    @Test
    public void equalsDespiteName() {
        final DataBlock data2 = new DataBlock("2", TYPE, SIZE);
        assertEquals(DATA, data2);
        assertEquals(DATA.hashCode(), data2.hashCode());
    }

    @Test
    public void notEquals() {
        final DataBlock data2 = new DataBlock(NAME, TYPE, 1L);
        assertNotEquals(DATA, data2);
        assertNotEquals(DATA.hashCode(), data2.hashCode());
    }

    @Test
    public void equalsBack() {
        final DataBlock data2 = new DataBlock(NAME, TYPE, SIZE);
        assertEquals(DATA, data2);
    }

    @Test
    public void equalsReflective() {
        assertEquals(DATA, DATA);
    }

    @Test
    public void equalsCopy() {
        final DataBlock data2 = new DataBlock(DATA);
        assertEquals(DATA, data2);
    }

    @Test
    public void notReferenceCopy() {
        final DataBlock data2 = new DataBlock(DATA);
        assertNotSame(DATA, data2);
    }

    @Test
    public void getType() {
        final DataBlock data2 = new DataBlock(NAME, TYPE, SIZE);
        assertEquals(TYPE, data2.getType());
    }

    @Test
    public void getSize() {
        final DataBlock data2 = new DataBlock(NAME, TYPE, SIZE);
        assertEquals(SIZE, data2.getSize().longValue());
    }

    @Test
    public void info() {
        assertNotNull(DATA.info());
    }

    @Test
    public void getId() {
        assertNotNull(DATA.getId());
    }

    @Test
    public void getName() {
        assertEquals(NAME, DATA.getName());
    }
}

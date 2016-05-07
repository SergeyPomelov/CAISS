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
import java.util.List;

import simulation.structures.commons.ComponentType;
import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 05/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class MemoryNodeTest {

    private static final String NAME = "memory";
    private static final List<DataBlock> DATA_BLOCKS = ImmutableList.of(
            new DataBlock("8bytes", DataType.EIGHT_B_FL, 1000L * 1000L));
    private static final BigDecimal CAPACITY = BigDecimal.valueOf(8L * 1000L * 1000L);

    static final MemoryNode MEMORY = new MemoryNode(NAME, DATA_BLOCKS, CAPACITY);

    @Test
    public void nullName() {
        passedIfException(() -> new MemoryNode(null, DATA_BLOCKS, CAPACITY));
    }

    @Test
    public void nullDataCapacity() {
        passedIfException(() -> new MemoryNode(NAME, null, CAPACITY));
    }

    @Test
    public void nullPhysicalCapacity() {
        passedIfException(() -> new MemoryNode(NAME, DATA_BLOCKS, null));
    }

    @Test
    public void copy() {
        final MemoryNode memory1 = new MemoryNode(MEMORY);
        assertNotSame(MEMORY, memory1);
        assertEquals(memory1.getDataCapacityList(), MEMORY.getDataCapacityList());
        assertEquals(memory1.getDataCapacity(0), MEMORY.getDataCapacity(0));
        assertEquals(memory1.getDataByteCapacity(), MEMORY.getDataByteCapacity());
    }

    @Test
    public void getDataCapacityList() {
        assertEquals(DATA_BLOCKS, MEMORY.getDataCapacityList());
    }

    @Test
    public void getDataCapacity() {
        assertEquals(DATA_BLOCKS.get(0), MEMORY.getDataCapacity(0));
    }

    @Test
    public void getDataCapacityMoreThanOne() {
        final List<DataBlock> dataBlocks1 =
                ImmutableList.of(new DataBlock("8bytes", DataType.EIGHT_B_FL, 1000L * 1000L),
                        new DataBlock("bool", DataType.BOOL, 100L * 1000L));
        final MemoryNode node = new MemoryNode(NAME, dataBlocks1, CAPACITY);
        assertEquals(node.getDataCapacity(0), new DataBlock("8bytes",
                DataType.EIGHT_B_FL, 1000L * 1000L));
        assertEquals(node.getDataCapacity(1), new DataBlock("bool", DataType.BOOL, 100L * 1000L));
    }

    @Test
    public void getDataCapacityBeyondSet() {
        passedIfException(() -> MEMORY.getDataCapacity(1));
    }

    @Test
    public void getDataByteCapacity() {
        assertEquals(CAPACITY, MEMORY.getDataByteCapacity());
    }

    @Test
    public void getArchitectureComponentType() {
        assertEquals(ArchitectureComponentType.MEMORY_NODE, MEMORY.getArchitectureComponentType());
    }

    @Test
    public void getComponentType() {
        assertEquals(ComponentType.ARCHITECTURE, MEMORY.getComponentType());
    }

    @Test
    public void getId() {
        assertNotNull(MEMORY.getId());
    }

    @Test
    public void getName() {
        assertEquals(NAME, MEMORY.getName());
    }

    @Test
    public void info() {
        assertNotNull(MEMORY.info());
    }
}

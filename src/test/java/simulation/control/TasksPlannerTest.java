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

package simulation.control;

import org.junit.Test;

import simulation.structures.architecture.ArchitectureBuilder;
import simulation.structures.architecture.ArithmeticNode;
import simulation.structures.architecture.Computer;
import simulation.structures.architecture.DataLink;
import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Gunt Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 17, 2015</pre>
 */
@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
public class TasksPlannerTest {

    @Test
    public void testLoad() {
        final TasksPlanner tasksPlanner = new TasksPlanner(ArchitectureBuilder.buildOneCore());
        assertTrue(tasksPlanner.getLog().isEmpty());
        assertEquals(1, tasksPlanner.getArchNodes().size());
        assertEquals(1, tasksPlanner.getMemoryNodes().size());
    }

    @Test
    public void testLoadIndependency() {
        final TasksPlanner tasksPlanner = new TasksPlanner(ArchitectureBuilder.buildOneCore());
        tasksPlanner.load(ArchitectureBuilder.buildOneCore());
        assertTrue(tasksPlanner.getLog().isEmpty());
        assertEquals(1, tasksPlanner.getArchNodes().size());
        assertEquals(1, tasksPlanner.getMemoryNodes().size());
        tasksPlanner.load(ArchitectureBuilder.buildTwoCore());
        assertTrue(tasksPlanner.getLog().isEmpty());
        assertEquals(2, tasksPlanner.getArchNodes().size());
        assertEquals(1, tasksPlanner.getMemoryNodes().size());
    }

    /**
     * Method: getJournal()
     */
    @Test
    public void testGetLog() {
        final TasksPlanner tasksPlanner = new TasksPlanner(ArchitectureBuilder.buildOneCore());
        final Computer comp = ArchitectureBuilder.buildOneCore();
        final ArithmeticNode core = tasksPlanner.getFreeArNode(comp.getArchNodes()).get();
        final DataBlock subMatrix = new DataBlock("subArray", DataType.FOUR_B_FL, 500L * 500L);
        final OperationWithData subInverse =
                new OperationWithData("inverse", OperationType.INVERSE, subMatrix);
        final OperationWithData transferSmall =
                new OperationWithData("inverse", OperationType.TRANSFER, subMatrix);
        final DataLink dataLink = comp.getArchitecture().iterator().next();

        tasksPlanner.load(comp);
        tasksPlanner.transfer(comp.getMemoryNodes().get(0), dataLink, core, transferSmall);
        tasksPlanner.calculate(core, subInverse);
        assertNotNull(tasksPlanner.getFreeArNode(comp.getArchNodes()));
        assertFalse(tasksPlanner.getLog().isEmpty());
    }

    @Test
    public void testFreeArNode() {
        final TasksPlanner tasksPlanner = new TasksPlanner(ArchitectureBuilder.buildOneCore());
        final Computer comp = ArchitectureBuilder.buildOneCore();
        assertNotNull(tasksPlanner.getFreeArNode(comp.getArchNodes()));
    }
} 

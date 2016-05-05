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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.algorithm.Algorithm;
import simulation.structures.algorithm.AlgorithmBuilder;
import simulation.structures.architecture.ArchitectureBuilder;
import simulation.structures.architecture.ArithmeticNode;
import simulation.structures.architecture.Computer;
import simulation.structures.architecture.DataLink;
import simulation.structures.commons.StructureElement;
import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;

/**
 * Class for control simulation process. Contains its logic.
 * @author Sergey Pomelov on 22/2/14.
 * @see TasksPlanner
 */
@Immutable
@ParametersAreNonnullByDefault
public final class SimulationController implements ISimulationController {

    private static final Logger log = LoggerFactory.getLogger(SimulationController.class);

    private static final long serialVersionUID = 2150290747465006705L;

    private final DataBlock subMatrix = new DataBlock("subArray", DataType.FOUR_B_FL, 500L * 500L);
    private final OperationWithData subInverse =
            new OperationWithData("inverse", OperationType.INVERSE, subMatrix);
    private final OperationWithData transferSmall =
            new OperationWithData("inverse", OperationType.TRANSFER, subMatrix);

    @Nonnull
    @Override
    public String simulate() {
        final StringBuilder out = new StringBuilder("");

        final Algorithm alg = AlgorithmBuilder.createAlgorithm();

        final Computer comp1 = ArchitectureBuilder.buildOneCore();
        final Computer comp2 = ArchitectureBuilder.buildTwoCore();
        final Computer comp3 = ArchitectureBuilder.buildFourCore();

        final TasksPlanner timeManager1 = new TasksPlanner(comp1);
        final TasksPlanner timeManager2 = new TasksPlanner(comp2);
        final TasksPlanner timeManager3 = new TasksPlanner(comp3);

        addElementsListInfo(alg.getStructure(), out, "ALGORITHM:");
        addElementsListInfo(comp1.getArchitecture(), out, "ARCHITECTURE:");

        doAndLogFourOperations(timeManager1, comp1, out, "TIMING 1Core:");
        doAndLogFourOperations(timeManager2, comp2, out, "TIMING 2Core:");
        doAndLogFourOperations(timeManager3, comp3, out, "TIMING 4Core:");

        return out.toString();
    }

    private static void addElementsListInfo(Iterable<? extends StructureElement> elements,
                                            StringBuilder out, String label) {
        addDivider(out, label);
        for (final StructureElement element : elements) {
            out.append(element.info());
        }
    }

    private static void addDivider(StringBuilder out, String label) {
        out.append(LS).append(LS).append("===================================================")
                .append(LS).append(label).append(LS);
    }

    private void doAndLogFourOperations(TasksPlanner timeManager, Computer comp, StringBuilder out,
                                        String label) {
        addDivider(out, label);
        doFourOperations(timeManager, comp, out);
    }

    private void doFourOperations(TasksPlanner timeManager, Computer comp, StringBuilder out) {
        for (int i = 1; i <= 4; i++) {
            doOperation(timeManager, comp);
        }
        out.append(timeManager.getLog()).append(timeManager.printTimings());
    }

    private boolean doOperation(TasksPlanner timeManager, Computer comp) {
        DataLink busDataLink = null;
        for (final DataLink dataLink : comp.getArchitecture()) {
            busDataLink = dataLink;
        }

        final Optional<ArithmeticNode> core = timeManager.getFreeArNode(comp.getArchNodes());
        if (core.isPresent()) {
            timeManager.transfer(comp.getMemoryNodes().get(0),
                    busDataLink, core.get(), transferSmall);
            timeManager.calculate(core.get(), subInverse);
            return true;
        } else {
            log.error("Can't find a free node in {}.", comp);
            return false;
        }
    }
}

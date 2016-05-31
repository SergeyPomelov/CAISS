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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.control.planner.TasksPlanner;
import simulation.structures.algorithm.Algorithm;
import simulation.structures.algorithm.AntAlgorithmBuilder;
import simulation.structures.algorithm.DataDependency;
import simulation.structures.architecture.AntsArchitectureBuilder;
import simulation.structures.architecture.CalculationNode;
import simulation.structures.architecture.Computer;
import simulation.structures.interaction.OperationWithData;

import static simulation.control.SimulationUtil.addElementsListInfo;
import static util.Constants.LS;

/**
 * @author Sergey Pomelov on 02/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
final class AntsSimulationController implements ISimulationController {

    private static final Logger log = LoggerFactory.getLogger(AntsSimulationController.class);
    private static final long serialVersionUID = 7895062954108800370L;

    @Nonnull
    @Override
    public String simulate() {
        final StringBuilder out = new StringBuilder("");

        final Algorithm algorithm = AntAlgorithmBuilder.createAntsAlgorithm(194, 1, 1);
        final Computer computer = (new AntsArchitectureBuilder(194L, 94_653L, 1, 1)).buildComputer(2);
        final TasksPlanner timeManager = new TasksPlanner(computer, TimeUnit.NANOSECONDS);

        addElementsListInfo(algorithm.getStructure(), out, "ALGORITHM:");
        addElementsListInfo(computer.getArchitecture(), out, "ARCHITECTURE:");

        algorithm.getStructure().forEach(dependency -> {
            List<DataDependency> localDependency = Collections.singletonList(dependency);
            do {
                localDependency = doOperationsReturnNext(localDependency, timeManager, computer);
            } while (!localDependency.isEmpty());
        });

        out.append(LS).append(timeManager.printHistory()).append(timeManager.printTimings());
        return out.toString();
    }


    private static void doSequentOperations(TasksPlanner timeManager, Computer computer,
                                            Iterable<OperationWithData> operations) {
        final Optional<CalculationNode> core =
                timeManager.getFreeCalculationNode(computer.getCalculationNodes());
        if (core.isPresent()) {
            for (OperationWithData operation : operations) {
                timeManager.calculate(core.get(), operation);
            }
        } else {
            log.error("Can't find a free node in {}!", computer);
        }
    }

    private static List<DataDependency> doOperationsReturnNext(Iterable<DataDependency> dependencies,
                                                               TasksPlanner timeManager,
                                                               Computer computer) {
        final List<DataDependency> nextStepDependencies = new ArrayList<>(0);
        dependencies.forEach(dependency -> {
            doSequentOperations(timeManager, computer, dependency.getOperations());
            nextStepDependencies.addAll(dependency.getNext());
        });
        timeManager.barrierNow();
        return nextStepDependencies;
    }

}

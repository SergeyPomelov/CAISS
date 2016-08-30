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
import java.util.Collection;
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
import static util.FormatUtil.formatTime;

/**
 * @author Sergey Pomelov on 02/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
final class AntsSimulationController implements ISimulationController {

    private static final Logger log = LoggerFactory.getLogger(AntsSimulationController.class);
    private static final long serialVersionUID = 7895062954108800370L;
    private static final long RUN_TIME_NS = 6_000_000_000_000L;
    private static final long RUNTIME_CLEARANCE_NS = 1_000_000_000L;
    private static final long MAX_RUNS = 1_000_000L;

    @Nonnull
    @Override
    public String simulate() {
        return simulateOne();
    }

    private static String simulateOne() {
        final StringBuilder out = new StringBuilder("");

        final Algorithm algorithm = AntAlgorithmBuilder.createAntsAlgorithm(194, 1, 1);
        final Computer computer = (new AntsArchitectureBuilder(194L, 94_653L, 1, 1)).buildComputer(2);
        final TasksPlanner timeManager = new TasksPlanner(computer, TimeUnit.NANOSECONDS);

        addElementsListInfo(algorithm.getStructure(), out, "ALGORITHM:");
        addElementsListInfo(computer.getArchitecture(), out, "ARCHITECTURE:");

        algorithm.getStructure().forEach(dependency -> {
            List<DataDependency> localDependency = Collections.singletonList(dependency);
            //noinspection MethodCallInLoopCondition, by design
            do {
                localDependency = doOperationsReturnNext(localDependency, timeManager, computer);
            } while (!localDependency.isEmpty());
        });

        out.append(LS).append(timeManager.printHistory()).append(timeManager.printTimings());
        return out.toString();
    }

    public static String simulateAll() {
        final Collection<SimulationResult> overallResults =
                new ArrayList<>(0);

        final long size = 194L;
        final int coloniesMax = 4;
        final int antsMax = 4;
        final int core = 2;
        final StringBuilder out = new StringBuilder("");
        for (int colonies = 1; colonies <= coloniesMax; colonies++) {
            for (int ants = 1; ants <= antsMax; ants++) {
                //noinspection ObjectAllocationInLoop
                overallResults.add(new SimulationResult(size, colonies, ants,
                        figureOutRunsAmount(size, colonies, ants, 2)));
            }
        }
        fillDataForValue(overallResults, out);
        return out.toString();
    }

    private static void fillDataForValue(Iterable<SimulationResult> overallResults, StringBuilder out) {
        out.append("runs = {");
        boolean firstValue = true;
        for (SimulationResult result : overallResults) {
            addValue(out, result, firstValue);
            firstValue = false;
        }
        out.append("};").append(LS);
    }

    private static void addValue(StringBuilder out, SimulationResult result, boolean firstValue) {
        if (!firstValue) {
            out.append(',');
        }
        out.append('{').append(result.getAnts()).append(',')
                .append(result.getColonies()).append(',')
                .append(result.getRuns())
                .append('}');
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

    @SuppressWarnings({"ObjectAllocationInLoop", "MethodCallInLoopCondition"})
    private static long figureOutRunsAmount(long size, int colonies, int ants, int cores) {
        final Algorithm algorithm = AntAlgorithmBuilder.createAntsAlgorithm(size, colonies, ants);

        long time = 0L;
        long sizeMax = MAX_RUNS;
        long sizeMin = 0L;
        long currentSize = (sizeMax - sizeMin) / 2;
        Computer computer = (new AntsArchitectureBuilder(size, currentSize, colonies, ants))
                .buildComputer(cores);
        TasksPlanner timeManager = new TasksPlanner(computer, TimeUnit.NANOSECONDS);
        while (Math.abs(RUN_TIME_NS - time) >= RUNTIME_CLEARANCE_NS) {
            // addElementsListInfo(algorithm.getStructure(), out, "ALGORITHM:");
            // addElementsListInfo(computer.getArchitecture(), out, "ARCHITECTURE:");

            time = runSimulation(algorithm, timeManager, computer);
            log.debug("({},{}), check:{} => result:{}", sizeMin, sizeMax, currentSize,
                    formatTime(time, TimeUnit.NANOSECONDS));

            if (time >= RUN_TIME_NS) {
                sizeMax = currentSize;
            } else {
                sizeMin = currentSize;
            }
            currentSize = sizeMin + ((sizeMax - sizeMin) / 2);

            computer = (new AntsArchitectureBuilder(size, currentSize, colonies, ants)).buildComputer(cores);
            timeManager = new TasksPlanner(computer, TimeUnit.NANOSECONDS);
        }

        return currentSize;
    }

    private static long runSimulation(Algorithm algorithm, TasksPlanner timeManager, Computer computer) {
        for (DataDependency dependency : algorithm.getStructure()) {
            List<DataDependency> localDependency = Collections.singletonList(dependency);
            //noinspection MethodCallInLoopCondition, by design
            do {
                localDependency = doOperationsReturnNext(localDependency, timeManager, computer);
            } while (!localDependency.isEmpty());
        }
        return timeManager.getMaxTime();
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

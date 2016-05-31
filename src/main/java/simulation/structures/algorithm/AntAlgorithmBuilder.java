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

package simulation.structures.algorithm;

import com.google.common.collect.ImmutableList.Builder;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

/**
 * @author Sergey Pomelov on 26/05/2016.
 */
@SuppressWarnings("ObjectAllocationInLoop")
@Immutable
public final class AntAlgorithmBuilder {

    private AntAlgorithmBuilder() { /* utility-class */ }

    /**
     * @return ACO linear algorithm
     */
    @Nonnull
    public static Algorithm createAntsAlgorithm(long dataSize, int colonies, int ants) {

        final DataBlock unknownData = new DataBlock("TSPData", DataType.UNKNOWN, 1L);
        final DataBlock graph = new DataBlock("TSPData", DataType.FOUR_B_FL, dataSize * dataSize);
        final DataBlock tour = new DataBlock("TSPData", DataType.FOUR_B_FL, dataSize);

        final List<OperationWithData> start = generateStartOperations(graph);
        final List<OperationWithData> coloniesStartStep = generateColonyOperations(unknownData);
        final List<OperationWithData> antsStep = generateAntOperations(graph, tour, unknownData);

        final List<DataDependency> antsAndColoniesDependencies =
                createAntsAndColoniesDependencies(colonies, ants, coloniesStartStep, antsStep);

        final DataDependency dependencyStart =
                new DataDependency("start calculations", DependencyType.DATA_TRUE,
                        start, antsAndColoniesDependencies);

        final List<DataDependency> listDep = new Builder<DataDependency>()
                .add(dependencyStart).build();

        return new Algorithm(listDep);
    }


    private static List<OperationWithData> generateStartOperations(DataBlock graph) {
        final OperationWithData readData =
                new OperationWithData("readAndPrepareData", OperationType.READ_DATA, graph);
        final OperationWithData preCalculations =
                new OperationWithData("preCalculations", OperationType.PRE_CALCULATIONS, graph);
        return new Builder<OperationWithData>().add(readData).add(preCalculations).build();
    }

    private static List<OperationWithData> generateColonyOperations(DataBlock unknownData) {
        final OperationWithData colonyRuleOverhead =
                new OperationWithData("colonyRuleOverhead", OperationType.OVERHEAD, unknownData);
        return new Builder<OperationWithData>().add(colonyRuleOverhead).build();
    }

    private static List<OperationWithData> generateAntOperations(DataBlock graph, DataBlock tour,
                                                                 DataBlock unknownData) {
        final OperationWithData systemOverhead =
                new OperationWithData("systemOverhead", OperationType.OVERHEAD, unknownData);
        final OperationWithData antSolutionGeneration =
                new OperationWithData("antSolutionGeneration",
                        OperationType.ANT_SOLUTION_GENERATION, tour);
        final OperationWithData antColonyInteraction =
                new OperationWithData("antColonyInteraction", OperationType.INTERACTION, graph);
        return new Builder<OperationWithData>()
                .add(systemOverhead).add(antSolutionGeneration).add(antColonyInteraction).build();
    }

    private static List<DataDependency> createAntsAndColoniesDependencies(int colonies, int ants,
                                                                          List<OperationWithData> coloniesStartStep,
                                                                          List<OperationWithData> antsStep) {
        final Builder<DataDependency> colonyStartStepBuilder = new Builder<>();
        for (int colonyIdx = 0; colonyIdx < colonies; colonyIdx++) {
            final DataDependency coloniesStartDependency = new DataDependency("colony-" + (colonyIdx + 1)
                    + " calculations", DependencyType.DATA_TRUE,
                    coloniesStartStep, createAntsCalculations(ants, antsStep));
            colonyStartStepBuilder.add(coloniesStartDependency);
        }
        return colonyStartStepBuilder.build();
    }

    private static List<DataDependency> createAntsCalculations(int ants, List<OperationWithData> antsStep) {
        final Builder<DataDependency> antStepBuilder = new Builder<>();
        for (int antIdx = 0; antIdx < ants; antIdx++) {
            final DataDependency antDependency = new DataDependency("ant-" + (antIdx + 1)
                    + " calculations", DependencyType.DATA_TRUE,
                    antsStep, Collections.emptyList());
            antStepBuilder.add(antDependency);
        }
        return antStepBuilder.build();
    }
}

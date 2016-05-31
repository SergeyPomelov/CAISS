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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationPerformance;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

/**
 * @author Sergey Pomelov on 27/05/2016.
 */
@SuppressWarnings("ObjectAllocationInLoop")
public final class AntsArchitectureBuilder {

    private static final MemoryNode memory = new MemoryNode("DDR2",
            Collections.singletonList(new DataBlock("8bytes", DataType.FOUR_B_FL, 256L * 1024L * 1024L)),
            BigDecimal.valueOf(1024L * 1024L * 1024L));
    private static final long templateAntsProblemSize = 194L;
    private final OperationPerformance readDataPerformance;
    private final OperationPerformance preCalculationsPerformance;
    private final OperationPerformance colonyRuleOverheadPerformance;
    private final OperationPerformance systemOverheadPerformance;
    private final OperationPerformance antSolutionGenerationPerformance;
    private final OperationPerformance antColonyInteractionPerformance;

    public AntsArchitectureBuilder(long problemSize, long projectedRuns, int colonies, int ants) {
        final DataBlock unknownData = new DataBlock("Unknown", DataType.UNKNOWN, 1L);
        final DataBlock graph = new DataBlock("TSPGraph", DataType.FOUR_B_FL, problemSize * problemSize);
        final DataBlock tour = new DataBlock("TSPTour", DataType.FOUR_B_FL, problemSize);

        final OperationWithData readData =
                new OperationWithData("readAndPrepareData", OperationType.READ_DATA, graph);
        final OperationWithData preCalculations =
                new OperationWithData("preCalculations", OperationType.PRE_CALCULATIONS, graph);
        final OperationWithData colonyRuleOverhead =
                new OperationWithData("colonyRuleOverhead", OperationType.OVERHEAD, unknownData);
        final OperationWithData systemOverhead =
                new OperationWithData("systemOverhead", OperationType.OVERHEAD, unknownData);
        final OperationWithData antSolutionGeneration =
                new OperationWithData("antSolutionGeneration", OperationType.ANT_SOLUTION_GENERATION, tour);
        final OperationWithData antColonyInteraction =
                new OperationWithData("antColonyInteraction", OperationType.INTERACTION, graph);

        readDataPerformance = new OperationPerformance("readDataPerformance", readData,
                predictPerformance(/*1_390_000_000L*/ 1L, templateAntsProblemSize, problemSize, 1.0D));
        preCalculationsPerformance = new OperationPerformance("preCalculationsPerformance", preCalculations,
                predictPerformance(/*148_000_000L*/ 1L, templateAntsProblemSize, problemSize, 2.0D));

        colonyRuleOverheadPerformance = new OperationPerformance("antsPerformance", colonyRuleOverhead,
                predictPerformance(17_725_000L * ants, templateAntsProblemSize, problemSize, -1.0D));

        systemOverheadPerformance = new OperationPerformance("systemOverhead", systemOverhead,
                predictPerformance(projectedRuns * 1_230_000L, projectedRuns, projectedRuns, 1.0D));

        antSolutionGenerationPerformance = new OperationPerformance("antSolutionGeneration",
                antSolutionGeneration, predictPerformance(projectedRuns * 6_391_000L, 15_000L,
                projectedRuns / ((long) colonies * ants), 1.0D));

        antColonyInteractionPerformance = new OperationPerformance("antColonyInteraction",
                antColonyInteraction, predictPerformance(projectedRuns * 3_652_000L, 15_000L,
                projectedRuns / ((long) colonies * ants), 1.0D));
    }

    /**
     * @return four core PC model
     */
    @Nonnull
    public Computer buildComputer(int coresAmount) {

        final List<OperationPerformance> speed = ImmutableList.of(
                readDataPerformance, preCalculationsPerformance, colonyRuleOverheadPerformance,
                systemOverheadPerformance, antSolutionGenerationPerformance, antColonyInteractionPerformance);

        final List<CalculationNode> cores = new ArrayList<>(coresAmount);
        for (int coreIdx = 0; coreIdx < coresAmount; coreIdx++) {
            cores.add(new CalculationNode("Core" + (coreIdx + 1), speed));
        }

        final List<TransferCapability> transferCapabilities = ImmutableList.of(
                new TransferCapability(DataType.FOUR_B_FL, 1_000_000, 1000L));
        final ArrayList<ArchitectureComponent> inIn =
                new ArrayList<>(Collections.singletonList(memory));
        final ArrayList<ArchitectureComponent> inOut =
                new ArrayList<>(cores);

        final DataLink bus = new DataLink("bus",
                new TransferCapabilities(transferCapabilities, 4000L), inIn, inOut);

        return new Computer(Collections.singletonList(bus));
    }

    private static long predictPerformance(long templatePerformance, long templateProblemSize,
                                           long problemSize, double algorithmComplexity) {
        //noinspection NumericCastThatLosesPrecision
        return (long) (templatePerformance * StrictMath.pow(((double) problemSize / templateProblemSize),
                algorithmComplexity));
    }
}

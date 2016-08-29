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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationPerformance;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

/**
 * @author Sergey Pomelov on 12/2/14.
 */
@Immutable
public final class ArchitectureBuilder implements Serializable {

    private static final long serialVersionUID = -5424200034980053066L;
    private static final String CORE_PERFORMANCE = "core performance";
    private static final String WHOLE_ARRAY = "wholeArray";
    private static final String INVERSE = "inverse";

    private static final MemoryNode memory = new MemoryNode("DDR2",
            Collections.singletonList(new DataBlock("8bytes", DataType.EIGHT_B_FL, 1000L * 1000L)),
            BigDecimal.valueOf(8L * 1000L * 1000L));

    private static final DataBlock matrix =
            new DataBlock(WHOLE_ARRAY, DataType.FOUR_B_FL, 1000L * 1000L);
    private static final DataBlock subMatrix =
            new DataBlock("subArray", DataType.FOUR_B_FL, 500L * 500L);

    private static final OperationWithData inverseLarge =
            new OperationWithData(INVERSE, OperationType.INVERSE, matrix);
    private static final OperationWithData inverseSmall =
            new OperationWithData(INVERSE, OperationType.INVERSE, subMatrix);

    private static final OperationPerformance inverseLargePerformance =
            new OperationPerformance(CORE_PERFORMANCE, inverseLarge,
                    size -> (long) (size / (float) inverseLarge.getData().getSize()) * 4000L);
    private static final OperationPerformance inverseSmallPerformance =
            new OperationPerformance(CORE_PERFORMANCE, inverseSmall,
                    size -> (long) (size / (float) inverseSmall.getData().getSize()) * 2000L);


    private static final List<OperationPerformance> speed;

    static {
        speed = ImmutableList.of(inverseSmallPerformance, inverseLargePerformance);
    }

    private ArchitectureBuilder() { /* utility class */ }

    /**
     * @return one core PC model
     */
    @Nonnull
    public static Computer buildFourCore() {

        final CalculationNode coreOne = new CalculationNode("Core1", speed);
        final CalculationNode coreTwo = new CalculationNode("Core2", speed);
        final CalculationNode coreThree = new CalculationNode("Core3", speed);
        final CalculationNode coreFour = new CalculationNode("Core4", speed);

        final List<TransferCapability> transferCapabilities = ImmutableList.of(
                new TransferCapability(DataType.FOUR_B_FL, 1_000_000, 1000L));

        final ArrayList<ArchitectureComponent> inIn =
                new ArrayList<>(Collections.singletonList(memory));
        final ArrayList<ArchitectureComponent> inOut =
                new ArrayList<>(Arrays.asList(coreOne, coreTwo, coreThree, coreFour));

        final DataLink bus =
                new DataLink("bus", new TransferCapabilities(transferCapabilities, 4000L), inIn, inOut);

        return new Computer(Collections.singletonList(bus));
    }

    /**
     * @return two core PC model
     */
    @Nonnull
    public static Computer buildTwoCore() {
        final CalculationNode coreOne = new CalculationNode("Core1", speed);
        final CalculationNode coreTwo = new CalculationNode("Core2", speed);

        final List<TransferCapability> transferCapabilities = ImmutableList.of(
                new TransferCapability(DataType.FOUR_B_FL, 1_000_000, 1000L));
        final ArrayList<ArchitectureComponent> inIn =
                new ArrayList<>(Collections.singletonList(memory));
        final ArrayList<ArchitectureComponent> inOut =
                new ArrayList<>(Arrays.asList(coreOne, coreTwo));

        final DataLink bus = new DataLink("bus",
                new TransferCapabilities(transferCapabilities, 4000L), inIn, inOut);

        return new Computer(Collections.singletonList(bus));
    }

    /**
     * @return four core PC model
     */
    @Nonnull
    public static Computer buildOneCore() {
        final CalculationNode coreOne = new CalculationNode("Core1", speed);

        final List<TransferCapability> transferCapabilities = ImmutableList.of(
                new TransferCapability(DataType.FOUR_B_FL, 1_000_000, 1000L));
        final ArrayList<ArchitectureComponent> inIn =
                new ArrayList<>(Collections.singletonList(memory));
        final ArrayList<ArchitectureComponent> inOut =
                new ArrayList<>(Collections.singletonList(coreOne));

        final DataLink bus = new DataLink("bus",
                new TransferCapabilities(transferCapabilities, 4000L), inIn, inOut);

        return new Computer(Collections.singletonList(bus));
    }
}

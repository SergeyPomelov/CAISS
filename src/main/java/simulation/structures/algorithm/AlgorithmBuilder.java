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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.DataType;
import simulation.structures.interaction.OperationType;
import simulation.structures.interaction.OperationWithData;

/**
 * Utility class for build some algorithm templates.
 * @author Sergey Pomelov on 12/2/14.
 */
@Immutable
public final class AlgorithmBuilder implements Serializable {

    private static final long serialVersionUID = 4170724840376687664L;

    private AlgorithmBuilder() { /* utility class */ }

    /**
     * @return parallel inversion template algorithm
     */
    @Nonnull
    public static Algorithm createAlgorithm() {

        final DataBlock subMatrix = new DataBlock("subArray", DataType.FOUR_B_FL, 500L * 500L);

        final OperationWithData subInverse =
                new OperationWithData("invSmall", OperationType.INVERSE, subMatrix);
        final OperationWithData transferSmall =
                new OperationWithData("tr", OperationType.TRANSFER, subMatrix);

        final ArrayList<OperationWithData> in = new ArrayList<>(4);
        final ArrayList<OperationWithData> out = new ArrayList<>(4);

        in.add(transferSmall);
        in.add(transferSmall);
        in.add(transferSmall);
        in.add(transferSmall);
        out.add(subInverse);
        out.add(subInverse);
        out.add(subInverse);
        out.add(subInverse);

        final DataDependency dependency =
                new DataDependency("data transfer", DependencyType.DATA_TRUE, in, out);
        final List<DataDependency> listDep = new ArrayList<>(1);
        listDep.add(dependency);

        return new Algorithm(listDep);
    }

    public static Algorithm createAntsAlgorithm() {

        final DataBlock subMatrix = new DataBlock("TSP_data", DataType.FOUR_B_FL, 194L);
        return null;
    }
}

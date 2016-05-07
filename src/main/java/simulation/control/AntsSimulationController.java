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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.algorithm.Algorithm;
import simulation.structures.algorithm.AlgorithmBuilder;
import simulation.structures.architecture.ArchitectureBuilder;
import simulation.structures.architecture.Computer;

/**
 * @author Sergey Pomelov on 02/05/2016.
 */
@Immutable
@ParametersAreNonnullByDefault
public class AntsSimulationController implements ISimulationController {

    private static final Logger log = LoggerFactory.getLogger(AntsSimulationController.class);
    private static final long serialVersionUID = 7895062954108800370L;

    @Nonnull
    @Override
    public String simulate() {
        final StringBuilder out = new StringBuilder("");

        final Algorithm alg = AlgorithmBuilder.createAntsAlgorithm();

        final Computer comp1 = ArchitectureBuilder.buildOneCore();
        final Computer comp2 = ArchitectureBuilder.buildTwoCore();
        final Computer comp3 = ArchitectureBuilder.buildFourCore();
        return out.toString();
    }
}

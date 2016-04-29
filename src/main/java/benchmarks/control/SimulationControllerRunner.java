/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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

package benchmarks.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simulation.control.SimulationController;

/**
 * @author Sergey Pomelov on 15/04/2016.
 */
public final class SimulationControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(SimulationControllerRunner.class);

    private SimulationControllerRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        log.info((new SimulationController()).simulate());
        System.exit(0);
    }
}

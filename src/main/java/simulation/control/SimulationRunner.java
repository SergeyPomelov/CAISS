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

import java.io.IOException;

import util.GNUCopyright;

import static util.ConversionUtil.bytesToMb;

/**
 * Runs algorithm & architecture simulation and prints logs.
 * @author Sergey Pomelov on 15/04/2016.
 */
final class SimulationRunner {

    private static final Logger log = LoggerFactory.getLogger(SimulationRunner.class);

    private SimulationRunner() { /* runnable class */ }

    public static void main(String... args) {
        log.info((new AntsSimulationController()).simulate());
        System.exit(0);
    }

    private static void printStats() {
        GNUCopyright.printLicence();
        log.info("Press to start. Cores: {}. Memory: {} Mb.",
                Runtime.getRuntime().availableProcessors(),
                bytesToMb(Runtime.getRuntime().maxMemory()));
    }

    private static void waitForInput() {
        int input = 0;
        try {
            input = System.in.read();
        } catch (IOException e) {
            log.error("{} {}", input, e.getStackTrace());
        }
    }
}

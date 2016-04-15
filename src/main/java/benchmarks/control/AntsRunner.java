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

import benchmarks.tasks.ants.AntsColony;

/**
 * @author Sergey Pomelov on 13/04/2016.
 */
final class AntsRunner {

    private static final Logger log = LoggerFactory.getLogger(AntsRunner.class);

    private AntsRunner() { /* runnable class */ }

    public static void main(String... args) {
        GNUCopyright.printLicence();
        log.info(AntsColony.getInstance().getLog());
    }
}

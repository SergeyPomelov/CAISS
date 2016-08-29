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

/**
 * @author Sergey Pomelov on 03/06/2016.
 */
class SimulationResult {
    private final long size;
    private final int colonies;
    private final int ants;
    private final long runs;

    SimulationResult(long size, int colonies, int ants, long runs) {
        this.size = size;
        this.colonies = colonies;
        this.ants = ants;
        this.runs = runs;
    }

    public long getSize() {
        return size;
    }

    public int getColonies() {
        return colonies;
    }

    public int getAnts() {
        return ants;
    }

    public long getRuns() {
        return runs;
    }
}

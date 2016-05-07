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

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static simulation.structures.architecture.DataLinkTest.LINK;
import static testutil.TestUtil.passedIfException;

/**
 * @author Sergey Pomelov on 07/05/2016.
 */
@SuppressWarnings("ConstantConditions")
public class ComputerTest {

    private static final List<DataLink> STRUCTURE = ImmutableList.of(LINK);
    private static final Computer COMPUTER = new Computer(STRUCTURE);

    @Test
    public void nullStructure() {
        passedIfException(() -> new Computer(null));
    }

    @Test
    public void getArchitecture() {
        assertEquals(STRUCTURE, COMPUTER.getArchitecture());
    }

    @Test
    public void getMemoryNodes() {
        assertEquals(STRUCTURE.get(0).getMemoryNodes(), COMPUTER.getMemoryNodes());
    }

    @Test
    public void getCalculationNodes() {
        assertEquals(STRUCTURE.get(0).getCalculationNodes(), COMPUTER.getCalculationNodes());
    }
}

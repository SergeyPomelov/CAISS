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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static util.ConversionUtil.nullFilter;

/**
 * Computation node or core.
 * @author Sergey Pomelov on 12/2/14.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
public final class Computer implements Serializable {
    private static final long serialVersionUID = -7080364917126661958L;
    @Nonnull
    private final List<DataLink> architecture;         // architecture graph
    @Nonnull
    private final List<ArithmeticNode> cores;      // arithmetic nodes list
    @Nonnull
    private final List<MemoryNode> memory;         // memory nodes list

    /** architecture graph */
    public Computer(@Nonnull Collection<DataLink> structure) {
        architecture = ImmutableList.copyOf(nullFilter(structure));

        final Collection<ArithmeticNode> tempArchNodes = new ArrayList<>(5);
        final Collection<MemoryNode> tempMemoryNodes = new ArrayList<>(1);
        for (final DataLink dataLink : architecture) {
            final Collection<ArithmeticNode> linkArNodes = dataLink.getArithmeticNodes();
            final Collection<MemoryNode> linkMemNodes = dataLink.getMemoryNodes();
            if (linkArNodes != null) {
                tempArchNodes.addAll(linkArNodes);
            }
            if (linkMemNodes != null) {
                tempMemoryNodes.addAll(linkMemNodes);
            }
        }

        cores = ImmutableList.copyOf(tempArchNodes);
        memory = ImmutableList.copyOf(tempMemoryNodes);
    }

    @Nonnull
    public Iterable<DataLink> getArchitecture() {
        return architecture;
    }

    @Nonnull
    public List<MemoryNode> getMemoryNodes() {
        return memory;
    }

    @Nonnull
    public Collection<ArithmeticNode> getArchNodes() {
        return cores;
    }
}

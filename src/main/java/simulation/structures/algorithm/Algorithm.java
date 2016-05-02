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


import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Graph like structure represents algorithm.
 * @author Sergey Pomelov on 12/2/14.
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
public final class Algorithm implements Serializable {

    private static final long serialVersionUID = -8563489136964513166L;

    /** algorithm graph */
    @Nonnull
    private final List<DataDependency> structure;

    public Algorithm(@Nonnull Collection<DataDependency> structure) {
        this.structure = ImmutableList.copyOf(structure.stream().filter(obj -> obj != null)
                .collect(Collectors.toList()));
    }

    @Nonnull
    public Iterable<DataDependency> getStructure() {
        return structure;
    }
}

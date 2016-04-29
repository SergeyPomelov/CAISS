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

package simulation.structures.algorithm;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import simulation.structures.interaction.DataBlock;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;

/**
 * @author Sergei Pomelov on 2.10.14 dependencies on algorithm steps a.k.a. data flow dependencies
 */
@SuppressWarnings("ReturnOfCollectionOrArrayField")
@Immutable
@ParametersAreNonnullByDefault
public final class DataDependency extends AlgorithmComponent {

    private static final long serialVersionUID = 988170536445679820L;
    private static final String LINK_CAP_OVERWHELMED = "Link cap overwhelmed";
    /** flow operation type */
    @Nonnull
    private final DependencyType type;
    /** flow start data */
    @Nonnull
    private final List<OperationWithData> in;
    /** flow end data */
    @Nonnull
    private final List<OperationWithData> out;

    public DataDependency(DataDependency init) {
        this(init.getName(), init.type, init.in, init.out);
    }


    /**
     * @param inName name of transfer operation
     * @param inType flow operation type
     * @param inIn   flow start data
     * @param inOut  low end data
     */
    public DataDependency(String inName, DependencyType inType,
                          Collection<OperationWithData> inIn, Collection<OperationWithData> inOut) {
        super(inName);
        type = DependencyType.valueOf(inType.name());
        in = ImmutableList.copyOf(inIn);
        out = ImmutableList.copyOf(inOut);
    }

    @Nonnull
    public OperationWithData getIn(int i) {
        if (i >= in.size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return in.get(i);
    }

    @Nonnull
    public OperationWithData getOut(int i) {
        if (i >= out.size()) {
            throw new IllegalArgumentException(LINK_CAP_OVERWHELMED);
        }
        return out.get(i);
    }

    @Nonnull
    public Collection<DataBlock> getUsedData() {
        final Collection<DataBlock> usedData = new ArrayList<>(10);
        final Collection<OperationWithData> all = new ArrayList<>(in);
        all.addAll(out);
        all.forEach(el -> usedData.add(el.getData()));
        return ImmutableList.copyOf(usedData);
    }

    @Nonnull
    @Override
    public String info() {
        final StringBuilder output = new StringBuilder(128);
        output.append(LS).append(LS).append("Dependency - ")
                .append(super.info()).append(':').append(type).append(':').append(LS);

        output.append(LS).append("In:").append(LS);
        for (final OperationWithData el : in) {
            output.append(String.format(" | %s", el.info()));
        }
        output.append(LS);

        output.append(LS).append("Out:").append(LS);
        for (final OperationWithData el : out) {
            output.append(String.format(" | %s", el.info()));
        }
        return output.toString();
    }

    @Nonnull
    List<OperationWithData> getIn() {
        return in;
    }

    @Nonnull
    List<OperationWithData> getOut() {
        return out;
    }

    @Nonnull
    DependencyType getType() {
        return type;
    }

}
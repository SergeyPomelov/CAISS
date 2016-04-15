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

package simulation.structures.interaction;


import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * @author Sergei Pomelov on 2.5.14.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class OperationWithData extends ComputingObject {

    private static final long serialVersionUID = 5318763246120708102L;
    @Nonnull
    private final OperationType type;                  // what we do
    @Nonnull
    private final DataBlock data;                      // with what


    OperationWithData() {
        this("ZeroOperation",
                OperationType.ADDITION,
                new DataBlock("ZeroDataBlock", DataType.BOOL, 0L));
    }

    public OperationWithData(OperationWithData init) {
        this(init.getName(), init.type, init.data);
    }

    /**
     * @param inType which operation
     * @param inData how fast we can do it
     */
    public OperationWithData(String inName, OperationType inType, DataBlock inData) {
        super(inName);
        if (inData.getSize() <= 0) {
            throw new IllegalArgumentException("Wrong inData for createAlgorithm Operation");
        }
        type = OperationType.valueOf(inType.name());
        data = new DataBlock(inData);
    }

    @Nonnull
    public OperationType getType() {
        return type;
    }

    @Nonnull
    public DataBlock getData() {
        return data;
    }

    @Nonnull
    @Override
    public String info() {
        return (String.format("%s-%s %s", super.info(), type, data.info()));
    }
}

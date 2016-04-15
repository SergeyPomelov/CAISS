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


import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * @author Sergei Pomelov on 2.5.14. Representation of how fast we can do this OperationwithData
 * @see OperationWithData
 */
@Immutable
@ParametersAreNonnullByDefault
public final class OperationPerformance extends ComputingObject {

    private static final long serialVersionUID = -5609898011539390298L;

    @Nonnull
    private final OperationWithData oper;                  // what we do
    @Nonnull
    @Nonnegative
    private final Long time;                  // how fast

    OperationPerformance() {
        this("ZeroOperationPerformance", new OperationWithData(), 0L);
    }

    public OperationPerformance(OperationPerformance inOperation) {
        this(inOperation.getName(), inOperation.oper, inOperation.time);
    }

    /**
     * @param inOper which operation
     * @param inTime how fast we can do it
     */
    public OperationPerformance(String inName, OperationWithData inOper, Long inTime) {
        super(inName);
        oper = new OperationWithData(inOper);
        time = inTime;
    }

    @Nonnull
    public OperationWithData getOperation() {
        return oper;
    }

    @Nonnull
    @Nonnegative
    public Long getTime() {
        return time;
    }

    @Nonnull
    @Override
    public String info() {
        return (String.format("%s(%s) t=%smc", super.info(), oper.info(), time.toString()));
    }
}

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

package simulation.control.planner;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static util.FormatUtil.formatTime;

/**
 * @author Sergey Pomelov on 30/05/2016.
 */
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
@Immutable
abstract class OperationRecord {
    private final long start;
    private final long end;
    @Nullable
    private final TimeUnit timeUnit;

    OperationRecord(long start, long end, @Nullable TimeUnit timeUnit) {
        this.start = start;
        this.end = end;
        this.timeUnit = timeUnit;
    }

    long getDuration() {
        return end - start;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Nonnull
    public Optional<TimeUnit> getTimeUnit() {
        return Optional.ofNullable(timeUnit);
    }

    @Override
    public String toString() {
        return formatTime(start, timeUnit) + "->" + formatTime(end, timeUnit);
    }
}

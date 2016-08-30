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

package benchmarks.metrics;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import util.Restrictions;

/**
 * @author Sergey Pomelov on 06/04/16.
 * @see PerformanceMeasurer
 */
@Immutable
@ParametersAreNonnullByDefault
public final class PerformanceRecord implements Serializable {

    private static final long serialVersionUID = -6427374776255656092L;

    @Nonnull
    private final String label;
    private final long time;
    private final long cpuTime;
    private final long userTime;

    PerformanceRecord(String label, long time, long cpuTime, long userTime) {
        Restrictions.ifContainsNullFastFail(label, time, cpuTime, userTime);
        this.label = label;
        this.time = time;
        this.cpuTime = cpuTime;
        this.userTime = userTime;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    public long getTime() {
        return time;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public long getUserTime() {
        return userTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("label", label)
                .add("time", time)
                .add("cpuTime", cpuTime)
                .add("userTime", userTime)
                .toString();
    }
}

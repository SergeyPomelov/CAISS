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

package util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Pomelov on 15/04/2016.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public final class TimeUtil {

    private TimeUtil() { /* utility class */ }

    public static long nanoToMls(long nano) {
        return TimeUnit.NANOSECONDS.toMillis(nano);
    }

    public static long secToNano(int seconds) {
        return TimeUnit.SECONDS.toNanos(seconds);
    }

    public static long mlsToNano(int milliseconds) {
        return TimeUnit.SECONDS.toNanos(milliseconds);
    }

    static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}

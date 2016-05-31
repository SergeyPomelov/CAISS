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

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

/**
 * @author Sergey Pomelov on 31/05/2016.
 */
public final class FormatUtil {

    private static final ThreadLocal<DecimalFormat> formatTime = new DecimalFormatThreadLocal();
    private static final ThreadLocal<DecimalFormat> formatPercent = new PercentFormatThreadLocal();

    private static final double NS_IN_SEC = 1_000_000_000.0D;
    private static final double MKS_IN_SEC = 1_000_000.0D;
    private static final double MS_IN_NS = 1_000.0D;
    private static final long TO_SEC_FOLD_CAP = 10_000_000_000L;
    private static final long TO_MS_FOLDING_CAP = 10_000_000L;
    private static final long TO_MKS_FOLDING_CAP = 10_000L;

    private FormatUtil() { /* utility class */ }

    public static String formatTime(long value, @Nullable TimeUnit unit) {
        if ((value < 0) || (unit == null)) {
            return String.valueOf(value);
        }

        final long nanos = unit.toNanos(value);
        if (nanos > TO_SEC_FOLD_CAP) {
            return '~' + formatTime.get().format(nanos / NS_IN_SEC) + 's';
        } else if (nanos > TO_MS_FOLDING_CAP) {
            return '~' + formatTime.get().format(nanos / MKS_IN_SEC) + "ms";
        } else if (nanos > TO_MKS_FOLDING_CAP) {
            return '~' + formatTime.get().format(nanos / MS_IN_NS) + "mks";
        } else {
            return nanos + "ns";
        }
    }

    public static String percentFormat(double v) {
        return formatPercent.get().format(v);
    }

    private static class PercentFormatThreadLocal extends ThreadLocal<DecimalFormat> {
        @Override
        protected DecimalFormat initialValue() {
            final DecimalFormat formatLocal = new DecimalFormat("##.####");
            formatLocal.setDecimalSeparatorAlwaysShown(false);
            return formatLocal;
        }
    }

    private static class DecimalFormatThreadLocal extends ThreadLocal<DecimalFormat> {
        @Override
        protected DecimalFormat initialValue() {
            final DecimalFormat formatLocal = new DecimalFormat(".##");
            formatLocal.setDecimalSeparatorAlwaysShown(false);
            return formatLocal;
        }
    }
}

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

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class for conversion methods.
 * @author Sergey Pomelov on 15/04/2016.
 */
@SuppressWarnings("ClassUnconnectedToPackage")
public final class ConversionUtil {

    private static final int BYTES_IN_MEGABYTES = 1024 * 1024;

    private ConversionUtil() { /* utility class */ }

    public static long bytesToMb(long bytes) {
        return bytes / BYTES_IN_MEGABYTES;
    }

    // by design throws NPE if the collection pointer is null
    public static <T> Collection<T> nullFilter(Collection<T> collection) {
        return collection.stream().filter(obj -> obj != null).collect(Collectors.toList());
    }
}

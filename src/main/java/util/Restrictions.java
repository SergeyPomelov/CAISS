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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Conditions checkers collection.
 * @author Sergey Pomelov on 15/04/2016.
 */
@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "ClassWithTooManyDependents", "ClassUnconnectedToPackage"})
public final class Restrictions {

    private static final Logger log = LoggerFactory.getLogger(Restrictions.class);

    private Restrictions() { /* util class */ }

    public static void ifContainsNullFastFail(Object... objects) {
        if (containsNull(objects)) {
            throw new IllegalArgumentException("Objects: " + Arrays.toString(objects)
                    + "contain null!");
        }
    }

    public static boolean containsNull(Object... objects) {
        for (final Object object : objects) {
            if (object == null) {
                log.error("Objects: {} contain null!", Arrays.toString(objects));
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ProhibitedExceptionThrown")
    public static <T> T ifNullFail(T object) {
        if (object == null) {
            throw new NullPointerException("Null pointer is forbidden here!");
        }
        return object;
    }

    public static <T extends Number> T ifNegativeFail(T number) {
        if ((number == null) || (number.doubleValue() < 0.0D)) {
            throw new IllegalArgumentException("Null or negative is forbidden here!");
        }
        return number;
    }

    public static void ifNotOnlyPositivesFastFail(Number... numbers) {
        if (containsNegatives(numbers)) {
            throw new IllegalArgumentException("Number: " + Arrays.toString(numbers)
                    + "contain negative or null value!");
        }
    }

    private static boolean containsNegatives(Number... numbers) {
        for (final Number number : numbers) {
            if ((number == null) || (number.doubleValue() < 0.0D)) {
                log.error("Numbers: {} contain negative or null!", Arrays.toString(numbers));
                return true;
            }
        }
        return false;
    }
}

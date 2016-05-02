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

import java.io.File;

/**
 * Project wide constants container.
 *
 * @author Sergey Pomelov on 12.03.15.
 */
public final class Constants {
    public static final String LS = System.lineSeparator().intern();
    public static final String FS = File.separator;
    public static final String GNU_COPYRIGHT_MSG =
            "    Computer and algorithm interaction simulation software (CAISS)." + LS +
                    "    Copyright (C) " + TimeUtil.getCurrentYear()
                    + " Pomelov Sergey Valer'evich." + LS +
                    "    This program comes with ABSOLUTELY NO WARRANTY." + LS +
                    "    This is free software, and you are welcome to redistribute it" + LS +
                    "    under certain conditions. " +
                    "For details go to <http://www.gnu.org/licenses/>.";

    private Constants() { /*utility class*/ }
}

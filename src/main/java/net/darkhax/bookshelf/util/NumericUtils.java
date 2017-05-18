/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Calendar;

public final class NumericUtils {

    /**
     * An array of all the LWJGL numeric key codes.
     */
    public static final int[] NUMERIC_KEYS = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 71, 72, 73, 75, 76, 77, 79, 80, 81 };

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private NumericUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Checks if a keyCode is numeric, meaning 0-9 on the keyboard or number pad.
     *
     * @param keyCode The key code to test.
     * @return boolean True, if the key is a number key.
     */
    public static boolean isKeyCodeNumeric (int keyCode) {

        for (final int validKey : NUMERIC_KEYS) {
            if (validKey == keyCode) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the code is being ran on a special day. If it is, the method will return true.
     * this can be used to allow special events to happen on specific days.
     *
     * @param month The month of the year to check for. January is 1 and December is 12.
     * @param day The day of the year to check for. The first day of the month is 1, the 15th
     *        day is 15 and so on.
     * @return Whether or not the current day is the one specified.
     */
    public static boolean isSpecialDay (int month, int day) {

        final Calendar today = Calendar.getInstance();
        return today.get(Calendar.MONTH) + 1 == month && today.get(Calendar.DAY_OF_MONTH) == day;
    }
}
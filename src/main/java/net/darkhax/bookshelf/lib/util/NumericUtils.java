package net.darkhax.bookshelf.lib.util;

import java.util.Calendar;

public class NumericUtils {

    /**
     * An array of all the LWJGL numeric key codes.
     */
    public static final int[] NUMERIC_KEYS = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 71, 72, 73, 75, 76, 77, 79, 80, 81 };

    /**
     * Checks if a keyCode is numeric, meaning 0-9 on the keyboard or number pad.
     *
     * @param keyCode The key code to test.
     * @return boolean True, if the key is a number key.
     */
    public static boolean isKeyCodeNumeric (int keyCode) {

        for (final int validKey : NUMERIC_KEYS)
            if (validKey == keyCode)
                return true;

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
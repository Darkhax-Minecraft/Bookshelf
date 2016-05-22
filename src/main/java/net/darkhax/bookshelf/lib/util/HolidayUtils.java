package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.lib.Constants;

import java.util.Calendar;

public class HolidayUtils {
    public static Boolean isNewYears = false;
    public static Boolean isAprilFirst = false;
    public static Boolean isHalloween = false;
    public static Boolean isChristmas = false;

    public static void checkDates () {

        dateChecker();
        if (isNewYears == true) {
            Constants.LOG.info("Happy New Year");
        }
        if (isAprilFirst == true) {
            Constants.LOG.info("Happy April Fools Day");
        }
        if (isHalloween == true) {
            Constants.LOG.info("Happy Halloween");
        }
        if (isChristmas == true) {
            Constants.LOG.info("Merry Christmas");
        }

    }

    public static void dateChecker () {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        switch (calendar.get(2)) {
            case 0: {
                if (calendar.get(5) == 1) {
                    isNewYears = true;
                }
                break;
            }
            case 3: {
                if (calendar.get(5) == 1) {
                    isAprilFirst = true;
                }
                break;
            }
            case 9: {
                if (calendar.get(5) == 31) {
                    isHalloween = true;
                }
                break;
            }
            case 11: {
                if (calendar.get(5) == 25) {
                    isChristmas = true;
                }
                break;
            }
        }
    }
}

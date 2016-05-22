package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.lib.Constants;

import java.util.Calendar;

public class HolidayUtils {
    public static Boolean IsNewYears = false;
    public static Boolean IsAprilFirst = false;
    public static Boolean IsHalloween = false;
    public static Boolean IsChristmas = false;

    public static void CheckDates () {

        DateChecker();
        if (IsNewYears == true) {
            Constants.LOG.info("Happy New Year");
        }
        if (IsAprilFirst == true) {
            Constants.LOG.info("Happy April Fools Day");
        }
        if (IsHalloween == true) {
            Constants.LOG.info("Happy Halloween");
        }
        if (IsChristmas == true) {
            Constants.LOG.info("Merry Christmas");
        }

    }

    public static void DateChecker () {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        switch (calendar.get(2)) {
            case 0: {
                if (calendar.get(5) == 1) {
                    IsNewYears = true;
                }
                break;
            }
            case 3: {
                if (calendar.get(5) == 1) {
                    IsAprilFirst = true;
                }
                break;
            }
            case 9: {
                if (calendar.get(5) == 31) {
                    IsHalloween = true;
                }
                break;
            }
            case 11: {
                if (calendar.get(5) == 25) {
                    IsChristmas = true;
                }
                break;
            }
        }
    }
}
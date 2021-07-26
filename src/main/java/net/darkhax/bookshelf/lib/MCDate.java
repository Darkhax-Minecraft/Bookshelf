/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

/**
 * This class is used to represent a date in the minecraft world.
 */
public class MCDate {

    /**
     * An array of all the days in a week.
     */
    public static final String[] NAMES_DAYS = { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" };

    /**
     * An array of all the months in a year.
     */
    public static final String[] NAMES_MONTHS = { "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december" };

    /**
     * An array of the total days in a month. Matching by index.
     */
    public static final int[] MONTH_LENGTHS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    /**
     * The length of a normal minecraft day in ticks.
     */
    public static final long DAY_LENGTH = 24000L;

    /**
     * The total amount of days.
     */
    private long totalDays = 0;

    /**
     * The year represented.
     */
    // https://www.youtube.com/watch?v=gkBt7yLXyDk
    private int year = 0;

    /**
     * The month represented.
     */
    private int month = 0;

    /**
     * The day represented.
     */
    private long day = 0;

    /**
     * Constructs a date for the current world time.
     *
     * @param world The world to get a date for. Different worlds may have different times.
     */
    public MCDate (World world) {

        this(world.getGameTime());
    }

    /**
     * Base constructor for MCDate.
     *
     * @param time The amount of ticks to represent.
     */
    public MCDate (long time) {

        this.totalDays = (time + DAY_LENGTH) / DAY_LENGTH;
        this.day = this.totalDays;

        while (this.day > MONTH_LENGTHS[this.month]) {

            this.day -= MONTH_LENGTHS[this.month];

            this.month++;

            if (this.month == MONTH_LENGTHS.length) {

                this.month = 0;
                this.year++;
            }
        }
    }

    /**
     * Gets the year represented.
     *
     * @return The represented year.
     */
    public int getYear () {

        return this.year;
    }

    /**
     * Gets the month represented.
     *
     * @return The represented month.
     */
    public int getMonth () {

        return this.month + 1;
    }

    /**
     * Gets the day represented.
     *
     * @return The represented day.
     */
    public long getDay () {

        return this.day;
    }

    /**
     * Gets the total days represented.
     *
     * @return The total amount of days.
     */
    public long getTotalDays () {

        return this.totalDays;
    }

    /**
     * Gets the name of the represented day in a week.
     *
     * @return The represented day of the week.
     */
    public String getDayName () {

        return NAMES_DAYS[(int) this.day];
    }

    /**
     * Gets the localized day name.
     *
     * @return The localized day name.
     */
    public String getLocalizedDayName () {

        return I18n.get("time.bookshelf.day." + this.getDayName() + ".name");
    }

    /**
     * Gets the name of the represented month.
     *
     * @return The name of the represented month.
     */
    public String getMonthName () {

        return NAMES_MONTHS[this.month];
    }

    /**
     * Gets the localized month name.
     *
     * @return The localized month name.
     */
    public String getLocalizedMonthName () {

        return I18n.get("time.bookshelf.month." + this.getMonthName() + ".name");
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.Random;

public final class Constants {

    // Mod Constants
    public static final String MOD_ID = "bookshelf";

    public static final String MOD_NAME = "Bookshelf";

    public static final String VERSION_NUMBER = "2.0.0";

    // System Constants
    public static final LoggingHelper LOG = new LoggingHelper(MOD_ID);

    public static final Random RANDOM = new Random();

    public static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private Constants () {

        throw new IllegalAccessError("Utility class");
    }
}

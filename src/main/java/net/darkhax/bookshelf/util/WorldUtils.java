/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.world.World;

public final class WorldUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java
     * adds an implicit public constructor to every class which does not define at
     * lease one explicitly. Hence why this constructor was added.
     */
    private WorldUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets the display name of a world.
     *
     * @param world The world to get the name of.
     * @return The name of the world.
     */
    public static String getWorldName (World world) {

        String result = "Unknown";

        // TODO add more fallback options
        if (world.provider != null) {

            result = world.provider.getDimensionType().getName();
        }

        return result;
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public final class GameUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an implicit
     * public constructor to every class which does not define at lease one explicitly. Hence why
     * this constructor was added.
     */
    private GameUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Checks if the game is running on the client or not.
     *
     * @return Whether or not the current thread is client sided.
     */
    public static boolean isClient () {

        return FMLCommonHandler.instance().getSide() == Side.CLIENT;
    }
}
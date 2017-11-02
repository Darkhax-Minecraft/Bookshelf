/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import javax.annotation.Nonnull;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class RegistryUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private RegistryUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets a registry name from a forge registerable object. The object can not be null. If
     * the registry name is null or empty an error will be reported.
     *
     * @param registerable The registerable object.
     * @return The id for the object.
     */
    public static String getRegistryId (@Nonnull IForgeRegistryEntry.Impl<?> registerable) {

        if (registerable.getRegistryName() == null || registerable.getRegistryName().toString().isEmpty()) {

            Constants.LOG.warn("Attempted to get ID for invalid registerable object. " + registerable.getClass().getName());
            return "";
        }

        return registerable.getRegistryName().toString();
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public final class WorldUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
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

    /**
     * Gets the amount of loaded chunks.
     *
     * @param world The world to get the chunk count of.
     * @return The amount of chunks. -1 means it was unable to get the amount.
     */
    public static int getLoadedChunks (WorldServer world) {

        return world.getChunkProvider() != null ? world.getChunkProvider().getLoadedChunkCount() : -1;
    }

    /**
     * Gets the dimension id of a world.
     *
     * @param world The world to get the id of.
     * @return The id of the world. 0 (surface) is used if none is found.
     */
    public static int getDimId (WorldServer world) {

        return world.provider != null ? world.provider.getDimension() : 0;
    }

    /**
     * Checks if two block positions are in the same chunk in a given world.
     *
     * @param world The world to check within.
     * @param first The first position.
     * @param second The second position.
     * @return Whether or not the two positions are in the same chunk.
     */
    public static boolean areSameChunk (World world, BlockPos first, BlockPos second) {

        final Chunk firstChunk = world.getChunkFromBlockCoords(first);
        final Chunk secondChunk = world.getChunkFromBlockCoords(second);
        return firstChunk.x == secondChunk.x && firstChunk.z == secondChunk.z;
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public final class WorldUtils {
    
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
     * Checks if two block positions are in the same chunk in a given world.
     *
     * @param first The first position.
     * @param second The second position.
     * @return Whether or not the two positions are in the same chunk.
     */
    public static boolean areSameChunk (BlockPos first, BlockPos second) {
        
        return new ChunkPos(first).equals(new ChunkPos(second));
    }
    
    /**
     * Attempts to set the biome of an entire chunk. Please note that this will also cause
     * conecting chunks to do an update, and will cause the targeted chunk to recieve a render
     * update.
     *
     * @param world The world to set the biome in.
     * @param pos The block position to target. This will target the chunk the psotion is in.
     * @param biome The biome to set the chunk to.
     */
    public static void setBiomes (World world, BlockPos pos, Biome biome) {
        
        try {
            
            final Chunk chunk = world.getChunk(pos);
            final Biome[] biomes = chunk.getBiomes();
            Arrays.fill(biomes, biome);
            chunk.markDirty();
            
            updateNearbyChunks(world, chunk, true, true);
        }
        
        catch (final Exception e) {
            
            Bookshelf.LOG.warn(e, "Unable to set biome for Pos: {}, Biome: {}", pos.toString(), biome.getRegistryName());
        }
    }
    
    /**
     * Marks a chunk for an update. This will set it dirty, and potentially do a render update.
     *
     * @param world The world to update chunks in.
     * @param chunk The chunk to update.
     * @param render Whether or not you want a render update.
     */
    public static void markChunkForUpdate (World world, Chunk chunk, boolean render) {
        
        chunk.markDirty();
        
        if (render) {
            
            final BlockPos initial = chunk.getPos().getBlock(1, 1, 1);
            world.markBlockRangeForRenderUpdate(initial, initial);
        }
    }
    
    /**
     * Updates all chunks near the provided chunk. This is a 3x3 area centered on the chunk.
     *
     * @param world The world to update chunks in.
     * @param chunk The chunk to update.
     * @param includeSelf Whether or not the base chunk should be updated.
     * @param render Whether or not you want a render update.
     */
    public static void updateNearbyChunks (World world, Chunk chunk, boolean includeSelf, boolean render) {
        
        for (final Chunk other : getNearbyChunks(world, chunk)) {
            
            if (other != chunk || includeSelf) {
                
                markChunkForUpdate(world, other, render);
            }
        }
    }
    
    /**
     * Gets a list of 9 chunks in a 3x3 area, centered around the passed chunk.
     *
     * @param world The world to get chunks from.
     * @param chunk The initial chunk.
     * @return A list of 9 chunks that are near the passed chunk, as well as the initial chunk.
     */
    public static List<Chunk> getNearbyChunks (World world, Chunk chunk) {
        
        return getNearbyChunks(world, chunk.getPos());
    }
    
    /**
     * Gets a list of 9 chunks in a 3x3 area, centered around the passed chunk position.
     *
     * @param world The world to get chunks from.
     * @param chunk The chunk position.
     * @return A list of 9 chunks that are near the passed chunk pos, as well as the chunk at
     *         the passed position.
     */
    public static List<Chunk> getNearbyChunks (World world, ChunkPos chunk) {
        
        final List<Chunk> chunks = new ArrayList<>();
        
        for (int offX = -1; offX < 2; offX++) {
            
            for (int offY = -1; offY < 2; offY++) {
                
                chunks.add(world.getChunk(chunk.x + offX, chunk.z + offY));
            }
        }
        
        return chunks;
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public final class WorldUtils {
    
    /**
     * Gets the amount of loaded chunks.
     *
     * @param world The world to get the chunk count of.
     * @return The amount of chunks. -1 means it was unable to get the amount.
     */
    public static int getLoadedChunks (ServerWorld world) {
        
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
    
    /**
     * Checks if a player is within distance of a block, and they are able to use it.
     * 
     * @param worldPos The world and position to test for.
     * @param player The player to test for.
     * @param statePredicate A test that is performed on the block at the specified position.
     * @param maxDistance The maximum distance allowed.
     * @return Whether or not the distance and predicate are valid.
     */
    public static boolean isWithinDistanceAndUsable (IWorldPosCallable worldPos, PlayerEntity player, Predicate<BlockState> statePredicate, double maxDistance) {
        
        return worldPos.applyOrElse( (world, pos) -> statePredicate.test(world.getBlockState(pos)) && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= maxDistance, true);
    }
    
    /**
     * Checks if a given position is within a slime chunk.
     * 
     * @param world The server world. Server is specifically needed as client doesn't have the
     *        seed.
     * @param pos The position to check.
     * @return Whether or not the given position is in a slime chunk.
     */
    public static boolean isSlimeChunk (ServerWorld world, BlockPos pos) {
        
        return SharedSeedRandom.seedSlimeChunk(pos.getX() >> 4, pos.getZ() >> 4, world.getSeed(), 987234911L).nextInt(10) == 0;
    }
    
    /**
     * Gets the water depth of a given entity based on it's eye height position.
     * 
     * @param living The entity to check the depth of.
     * @param toAir Whether or not non-air blocks also count.
     * @return The depth of the given entity.
     */
    public static int getWaterDepth (LivingEntity living, boolean toAir) {
        
        return getWaterDepth(living.world, new BlockPos(living.getPosX(), living.getPosY() + living.getEyeHeight(), living.getPosZ()), toAir);
    }
    
    /**
     * Gets the depth of a given position within water. Scans upwards to find the surface.
     * 
     * @param world The world instance.
     * @param startingPos The starting scan position.
     * @param toAir Whether or not non-air blocks also count.
     * @return The depth of the given position.
     */
    public static int getWaterDepth (World world, BlockPos startingPos, boolean toAir) {
        
        final BlockPos.Mutable depthPos = startingPos.func_239590_i_();
        
        int depth = 0;
        
        while (!World.isOutsideBuildHeight(depthPos) && (world.hasWater(depthPos) || toAir && !world.isAirBlock(depthPos))) {
            
            depth++;
            depthPos.move(Direction.UP);
        }
        
        return depth;
    }
}
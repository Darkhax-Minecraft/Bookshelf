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
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
     * Gets a map of recipes for a given type from the recipe manager of the world.
     * 
     * @param recipeType The type of recipe to look for.
     * @param manager The recipe manager instance.
     * @return A map of all recipes for the provided recipe type. This will be null if no
     *         recipes were registered.
     */
    @Nullable
    public static Map<ResourceLocation, IRecipe<?>> getRecipes (IRecipeType<?> recipeType, RecipeManager manager) {
        
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, manager, "field_199522_d");
        return recipesMap.getOrDefault(recipeType, ImmutableMap.of());
    }
}
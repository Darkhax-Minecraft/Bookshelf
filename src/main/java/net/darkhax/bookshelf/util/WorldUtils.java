/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
     * Looks up the map of all known recipes for a given recipe type.
     * 
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @return A map of recipes for the provided recipe type. Key is ResourceLocation, value is
     *         the recipe object.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends IRecipe<?>> Map<ResourceLocation, T> getRecipes (IRecipeType<T> recipeType, RecipeManager manager) {
        
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, manager, "field_199522_d");
        return (Map) recipesMap.getOrDefault(recipeType, Collections.emptyMap());
    }
    
    /**
     * Gets a list of all recipes for a given recipe type. This list will be sorted using the
     * translation key of the output item.
     * 
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @return A list of recipes for the given recipe type.
     */
    public static <T extends IRecipe<?>> List<T> getRecipeList (IRecipeType<T> recipeType, RecipeManager manager) {
        
        return getRecipeList(recipeType, manager, Comparator.comparing(recipe -> recipe.getRecipeOutput().getTranslationKey()));
    }
    
    /**
     * Gets a list of all recipes for a given recipe type. This list will be sorted using the
     * provided comparator.
     * 
     * @param recipeType The recipe type to look up.
     * @param manager The recipe manager to pull data from.
     * @param comparator A comparator that will be used to sort the map.
     * @return A list of recipes for the given recipe type.
     */
    public static <T extends IRecipe<?>> List<T> getRecipeList (IRecipeType<T> recipeType, RecipeManager manager, Comparator<T> comparator) {
        
        return getRecipes(recipeType, manager).values().stream().sorted(comparator).collect(Collectors.toList());
    }
}
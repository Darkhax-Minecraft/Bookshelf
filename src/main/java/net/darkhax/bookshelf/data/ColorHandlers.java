/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.data;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A client side only class, which contains references to reusable color handlers for items and
 * blocks.
 */
@OnlyIn(Dist.CLIENT)
public class ColorHandlers {
    
    /**
     * A reusable color handler which uses the hash of
     * {@link net.minecraft.item.ItemStack#getDisplayName()}.
     */
    public static final IItemColor ITEM_DISPLAY_NAME = (stack, index) -> stack.getDisplayName().hashCode();
    
    /**
     * A reusable color handler which uses the hash of {@link ItemStack#getTranslationKey()}.
     */
    public static final  IItemColor ITEM_UNLOCALIZED_NAME = (stack, index) -> stack.getTranslationKey().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.item.ItemStack#toString()}.
     */
    public static final IItemColor ITEM_TO_STRING = (stack, index) -> stack.toString().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.item.Item#getRegistryName()}.
     */
    public static final IItemColor ITEM_IDENTIFIER = (stack, index) -> stack.getItem().getRegistryName().toString().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.nbt.CompoundNBT#toString()}.
     */
    public static final IItemColor ITEM_NBT = (stack, index) -> stack.getTag().toString().hashCode();
    
    // Blocks
    
    /**
     * A reusable color handler which applies the foliage color for the biome.
     */
    public static final IBlockColor BLOCK_FOLIAGE = (state, world, pos, index) -> BiomeColors.getFoliageColor(world, pos);

    /**
     * A reusable color handler which applies the grass color for the biome.
     */
    public static final IBlockColor BLOCK_GRASS = (state, world, pos, index) -> BiomeColors.getGrassColor(world, pos);
    
    /**
     * A reusable color handler which applies the water color for the biome.
     */
    public static final IBlockColor BLOCK_WATER = (state, world, pos, index) -> BiomeColors.getWaterColor(world, pos);
}
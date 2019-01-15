/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.data;

import net.darkhax.bookshelf.lib.MCColor;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A client side only class, which contains references to reusable color handlers for items and
 * blocks.
 */
@SideOnly(Side.CLIENT)
public class ColorHandlers {
    
    /**
     * A reusable color handler which uses the hash of
     * {@link net.minecraft.item.ItemStack#getDisplayName()}.
     */
    public static IItemColor ITEM_DISPLAY_NAME = (stack, index) -> stack.getDisplayName().hashCode();
    
    /**
     * A reusable color handler which uses the hash of {@link ItemStack#getTranslationKey()}.
     */
    public static IItemColor ITEM_UNLOCALIZED_NAME = (stack, index) -> stack.getTranslationKey().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.item.ItemStack#toString()}.
     */
    public static IItemColor ITEM_TO_STRING = (stack, index) -> stack.toString().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.item.Item#getRegistryName()}.
     */
    public static IItemColor ITEM_IDENTIFIER = (stack, index) -> stack.getItem().getRegistryName().toString().hashCode();
    
    /**
     * A reusable color handler which uses the hash of the
     * {@link net.minecraft.nbt.NBTTagCompound#toString()}.
     */
    public static IItemColor ITEM_NBT = (stack, index) -> stack.getTagCompound().toString().hashCode();
    
    /**
     * A reusable color handler which uses a MCColor which is read from
     * {@link net.minecraft.item.ItemStack#getTagCompound()};
     */
    public static IItemColor ITEM_MCCOLOR = (stack, index) -> MCColor.isAcceptable(stack) ? new MCColor(stack).getRGB() : MCColor.WHITE.getRGB();
    
    // Blocks
    
    /**
     * A reusable color handler which uses a MCColor which is read from
     * {@link net.minecraft.tileentity.TileEntity#getTileData()}.
     */
    public static IBlockColor BLOCK_MCCOLOR = (state, world, pos, index) -> MCColor.isAcceptable(world, pos) ? new MCColor(world, pos).getRGB() : MCColor.WHITE.getRGB();
    
    /**
     * A reusable color handler which applies the foliage color for the biome.
     */
    public static IBlockColor BLOCK_FOLIAGE = (state, world, pos, index) -> BiomeColorHelper.getFoliageColorAtPos(world, pos);
    
    /**
     * A reusable color handler which applies the grass color for the biome.
     */
    public static IBlockColor BLOCK_GRASS = (state, world, pos, index) -> BiomeColorHelper.getGrassColorAtPos(world, pos);
    
    /**
     * A reusable color handler which applies the water color for the biome.
     */
    public static IBlockColor BLOCK_WATER = (state, world, pos, index) -> BiomeColorHelper.getWaterColorAtPos(world, pos);
}
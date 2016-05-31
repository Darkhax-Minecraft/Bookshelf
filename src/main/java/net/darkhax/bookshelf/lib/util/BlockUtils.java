package net.darkhax.bookshelf.lib.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

public class BlockUtils {
    
    /**
     * Checks if an ItemStack contains an ore item. This is done by checking the item extends
     * BlockOre, or if the ore dictionary for entries that start with 'ore'. It will also check
     * the display name of the stack to see if it has the word Ore in it.
     * 
     * @param stack The ItemStack to check.
     * @return Whether or not the ItemStack is an ore.
     */
    public static boolean isOre (ItemStack stack) {
        
        if (stack == null || stack.getItem() == null)
            return false;
            
        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockOre)
            return true;
            
        for (final int oreID : OreDictionary.getOreIDs(stack))
            if (OreDictionary.getOreName(oreID).startsWith("ore"))
                return true;
                
        if (stack.getDisplayName().matches(".*(^|\\s)([oO]re)($|\\s)."))
            return true;
            
        return false;
    }
    
    /**
     * Checks if a block is a fluid or not.
     *
     * @param block An instance of the block being checked.
     * @return If the block is a fluid, true will be returned. If not, false will be returned.
     */
    public static boolean isFluid (Block block) {
        
        return block == Blocks.LAVA || block == Blocks.WATER || block instanceof IFluidBlock;
    }
}
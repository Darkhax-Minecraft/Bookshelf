package net.darkhax.bookshelf.lib.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockUtils {
    
    /**
     * Checks if a block is a fluid or not.
     *
     * @param block: An instance of the block being checked.
     * @return boolean: If the block is a fluid, true will be returned. If not, false will be
     *         returned.
     */
    public static boolean isFluid (Block block) {
        
        return block == Blocks.LAVA || block == Blocks.WATER || block instanceof IFluidBlock;
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class InventoryUtils {
    
    /**
     * Gets an inventory from a position within the world. If no tile exists or the tile does
     * not have the inventory capability the empty inventory handler will be returned.
     * 
     * @param world The world instance.
     * @param pos The position of the expected tile entity.
     * @param side The side to access the inventory from.
     * @return The inventory handler. Will be empty if none was found.
     */
    public static IItemHandler getInventory (World world, BlockPos pos, Direction side) {
        
        final TileEntity tileEntity = world.getTileEntity(pos);
        
        if (tileEntity != null) {
            
            final LazyOptional<IItemHandler> inventoryCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            return inventoryCap.orElse(EmptyHandler.INSTANCE);
        }
        
        return EmptyHandler.INSTANCE;
    }
    
    /**
     * Checks if a position has an inventory. The empty inventory handler will be considered
     * invalid.
     * 
     * @param world The world instance.
     * @param pos The position of the expected tile entity.
     * @param side The side to access the inventory from.
     * @return Whether or not the inventory exists.
     */
    public static boolean hasInventory (World world, BlockPos pos, Direction side) {
        
        final IItemHandler handler = getInventory(world, pos, side);
        return handler != null && handler != EmptyHandler.INSTANCE;
    }
}
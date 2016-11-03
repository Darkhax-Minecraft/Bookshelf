package net.darkhax.bookshelf.lib.modutils.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public class BaublesUtils {
    
    public static final int AMULTER = 0;
    public static final int RING_1 = 1;
    public static final int RING_2 = 2;
    public static final int BELT = 3;
    public static final int HEAD = 4;
    public static final int BODY = 5;
    public static final int CHARM = 6;
    
    /**
     * Attempts to get a bauble from the player.
     * 
     * @param player The player to get the bauble from.
     * @param type The type of bauble being searched for.
     * @return The first stack found.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getBauble (EntityPlayer player, BaubleType type) {
        
        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
        
        for (final int slotId : type.getValidSlots())
            if (inv != null) {
                
                final ItemStack stack = inv.getStackInSlot(slotId);
                
                if (stack != null)
                    return stack;
            }
        
        return null;
    }
    
    /**
     * Attempts to get a bauble from the player.
     * 
     * @param player The player to get the bauble from.
     * @param type The slot to get the bauble from.
     * @return Gets the stack in the slot.
     */
    public static ItemStack getBauble (EntityPlayer player, int type) {
        
        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
        
        if (inv != null) {
            
            final ItemStack stack = inv.getStackInSlot(type);
            
            if (stack != null)
                return stack;
        }
        
        return null;
    }
    
    /**
     * Checks if a player has a bauble in their inventory.
     * 
     * @param player The player to get the bauble from.
     * @param stack The stack to search for.
     * @param type The type of bauble being searched for.
     * @return Whether or not a valid stack was found.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean hasBauble (EntityPlayer player, ItemStack stack, BaubleType type) {
        
        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
        
        for (final int slotId : type.getValidSlots())
            if (inv != null && ItemStackUtils.areStacksSimilar(stack, inv.getStackInSlot(slotId)))
                return true;
            
        return false;
    }
    
    /**
     * Equips a bauble in the desired slot if one does not already exist.
     * 
     * @param player The player to give the ring to.
     * @param item The item to set in the slot.
     * @param slot The slot to place the bauble in. 0 is amulet, 1 and 2 are rings. 3 is belt.
     * @return Whether or not the bauble was equipped.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean equipBauble (EntityPlayer player, ItemStack item, int slot) {
        
        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
        if (inv != null) {
            final ItemStack existing = inv.getStackInSlot(slot);
            if (existing == null) {
                inv.setStackInSlot(slot, item.copy());
                item.stackSize--;
                return true;
            }
        }
        return false;
    }
}

package net.darkhax.bookshelf.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDisabled extends Slot {
    
    /**
     * Creates an inventory slot that is completely disabled. Players will not be able to add
     * or remove items from this slot.
     * 
     * @param inventory: The inventory that this slot is a part of.
     * @param index: The index of this slot within the inventory.
     * @param posX: The X position of this slot within the inventory screen.
     * @param posY: The Y position of this slot within the inventory screen.
     */
    public SlotDisabled(IInventory inventory, int index, int posX, int posY) {
        
        super(inventory, index, posX, posY);
    }
    
    @Override
    public boolean isItemValid (ItemStack inStack) {
        
        return false;
    }
    
    @Override
    public boolean canTakeStack (EntityPlayer outStack) {
        
        return false;
    }
}
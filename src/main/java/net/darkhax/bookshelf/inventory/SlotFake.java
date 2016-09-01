package net.darkhax.bookshelf.inventory;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFake extends Slot {
    
    public SlotFake(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    /**
     * Must be called for custom logic to be applied. This can be done by overriding
     * {@link Container#slotClick(int, int, ClickType, EntityPlayer)} and checking if the slot
     * clicked is a fake one. If it is, return this method call.
     * 
     * @param container The container the slot is in.
     * @param slotId The Id of the slot.
     * @param dragType The drag type.
     * @param type the click type.
     * @param player The player.
     * @return The resulting item.
     */
    public ItemStack slotClicked (Container container, int slotId, int dragType, ClickType type, EntityPlayer player) {
        
        putStack(player.inventory.getItemStack());
        container.detectAndSendChanges();
        return (player.inventory.getItemStack() != null) ? player.inventory.getItemStack() : null;
    }
    
    @Override
    public void putStack (ItemStack stack) {
        
        if (ItemStackUtils.isValidStack(stack)) {
            
            stack = stack.copy();
            stack.stackSize = 1;
        }
        
        super.putStack(stack);
    }
    
    @Override
    public boolean isItemValid (ItemStack stack) {
        
        return false;
    }
    
    @Override
    public boolean canTakeStack (EntityPlayer player) {
        
        return false;
    }
    
    @Override
    public ItemStack decrStackSize (int amount) {
        
        return null;
    }
}

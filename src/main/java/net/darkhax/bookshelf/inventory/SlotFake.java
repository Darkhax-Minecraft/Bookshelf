package net.darkhax.bookshelf.inventory;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFake extends Slot {

    /**
     * Creates a slot which acts as a fake/ghost/filter slot. When an item is placed inside,
     * only a copy with a stack size of one is placed. When it is removed, the copy is deleted.
     * Perfect for creating item filters.
     *
     * @param inventory The inventory to add the slot to.
     * @param index The slot index.
     * @param x The X position of the slot.
     * @param y The Y position of the slot.
     */
    public SlotFake (IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeStack (EntityPlayer playerIn) {

        this.putStack(playerIn.inventory.getItemStack());
        return false;
    }

    @Override
    public boolean isItemValid (ItemStack stack) {

        this.putStack(stack.copy());
        return false;
    }

    @Override
    public ItemStack decrStackSize (int amount) {

        this.putStack(null);
        return null;
    }

    @Override
    public void putStack (@Nullable ItemStack stack) {

        if (ItemStackUtils.isValidStack(stack)) {

            stack = stack.copy();
            stack.stackSize = 1;
        }

        super.putStack(stack);
    }
}
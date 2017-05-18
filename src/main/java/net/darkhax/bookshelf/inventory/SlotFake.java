/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.inventory;

import javax.annotation.Nullable;

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

        ItemStack copy = stack;

        if (!copy.isEmpty()) {

            copy = copy.copy();
            copy.setCount(1);
        }

        super.putStack(copy);
    }
}
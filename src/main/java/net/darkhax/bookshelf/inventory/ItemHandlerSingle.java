package net.darkhax.bookshelf.inventory;

import net.minecraft.item.ItemStack;

/**
 * An item handler that only has a single slot. Provides some nice helper functions that take
 * into account the size of the inventory for you. This class provides a good basis for
 * input/output inventories that only have one slot.
 */
public class ItemHandlerSingle extends ItemHandler<ItemHandlerSingle> {

    /**
     * Creates a new item handler that only contains a single slot.
     */
    public ItemHandlerSingle () {

        super(1);
    }

    /**
     * Gets the stack held by the inventory.
     *
     * @return The stack held by the inventory.
     */
    public ItemStack getStack () {

        return this.getStackInSlot(0);
    }

    /**
     * Sets the stack held by the inventory. This will bypass
     * {@link #isItemValid(int, ItemStack)}.
     *
     * @param stack The stack to put in the inventory.
     */
    public void setStack (ItemStack stack) {

        this.setStackInSlot(0, stack);
    }

    /**
     * Checks if the inventory is empty.
     *
     * @return Whether or not the inventory is empty.
     */
    public boolean isEmpty () {

        return this.getStack().isEmpty();
    }

    /**
     * Destroys the stack contained by the inventory and then replaces it with
     * {@link ItemStack#EMPTY}.
     */
    public void clear () {

        this.getStack().setCount(0);
        this.setStack(ItemStack.EMPTY);
    }

    /**
     * Creates a copy of the stack within the inventory.
     *
     * @return A copy of the stack within the inventory.
     */
    public ItemStack copy () {

        return this.getStack().copy();
    }

    @Override
    protected void validateSlotIndex (int slot) {

        if (slot != 0) {

            throw new IllegalStateException("This inventory only has one slot. Expected 0 and got slotID " + slot);
        }
    }

    @Override
    public void setSize (int size) {

        if (size != 1) {
            throw new IllegalStateException("Tried to set inv size to " + size + " but this inv type is strictly 1.");
        }

        super.setSize(size);
    }

    @Override
    public ItemHandlerSingle self () {

        return this;
    }
}
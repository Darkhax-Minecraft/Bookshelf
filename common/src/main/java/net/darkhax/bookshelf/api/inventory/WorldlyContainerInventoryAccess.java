package net.darkhax.bookshelf.api.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

public class WorldlyContainerInventoryAccess<T extends WorldlyContainer> extends ContainerInventoryAccess<T> {

    public WorldlyContainerInventoryAccess(T internal) {

        super(internal);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {

        return this.internal.getSlotsForFace(side);
    }

    @Override
    public ItemStack insert(int slot, ItemStack insertStack, Direction side, boolean modify) {

        final ItemStack existingStack = this.getStackInSlot(slot);

        if (!this.internal.canPlaceItemThroughFace(slot, existingStack, side)) {

            return insertStack;
        }

        return super.insert(slot, insertStack, side, modify);
    }

    @Override
    public ItemStack extract(int slot, int amount, Direction side, boolean modify) {

        final ItemStack existingStack = this.getStackInSlot(slot);

        if (!this.internal.canTakeItemThroughFace(slot, existingStack, side)) {

            return ItemStack.EMPTY;
        }

        return super.extract(slot, amount, side, modify);
    }
}
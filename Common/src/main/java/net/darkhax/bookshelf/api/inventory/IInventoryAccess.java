package net.darkhax.bookshelf.api.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface IInventoryAccess {

    boolean isEmpty();

    int getSize();

    ItemStack getItemInSlot(int slotId);

    boolean canInsert(int slotId, ItemStack stack);

    void setItemInSlot(int slotId, ItemStack stack);

    int getMaxStackSize(int slotId);

    int[] getAvailableSlots(Direction side);

    boolean canInsert(int slotId, ItemStack toInsert, Direction side);

    boolean canExtract(int slotId, int toExtract, Direction side);
}
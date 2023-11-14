package net.darkhax.bookshelf.impl.inventory;

import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.stream.IntStream;

public class ItemHandlerInventoryAccess implements IInventoryAccess {

    private final IItemHandler internal;
    private final CachedSupplier<int[]> availableSlots;

    public ItemHandlerInventoryAccess(IItemHandler internal) {

        this.internal = internal;
        this.availableSlots = CachedSupplier.cache(() -> IntStream.range(0, internal.getSlots()).toArray());
    }

    @Override
    public int[] getAvailableSlots(Direction side) {

        return this.availableSlots.get();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return this.internal.getStackInSlot(slot);
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, Direction side, boolean modify) {

        return this.internal.insertItem(slot, stack, !modify);
    }

    @Override
    public ItemStack extract(int slot, int amount, Direction side, boolean modify) {
        return this.extract(slot, amount, side, !modify);
    }

    @Override
    public int getSlotSize(int slot) {

        return this.internal.getSlotLimit(slot);
    }

    @Override
    public boolean isValidForSlot(int slot, ItemStack stack, Direction side) {

        return this.internal.isItemValid(slot, stack);
    }
}

package net.darkhax.bookshelf.api.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

import java.util.stream.IntStream;

public class InventoryAccessVanilla implements IInventoryAccess {

    private final Container container;

    public InventoryAccessVanilla(Container container) {

        this.container = container;
    }

    @Override
    public boolean isEmpty() {

        return this.container.isEmpty();
    }

    @Override
    public int getSize() {

        return this.container.getContainerSize();
    }

    @Override
    public ItemStack getItemInSlot(int slotId) {

        return this.container.getItem(slotId);
    }

    @Override
    public boolean canInsert(int slotId, ItemStack stack) {

        return this.container.canPlaceItem(slotId, stack);
    }

    @Override
    public void setItemInSlot(int slotId, ItemStack stack) {

        this.container.setItem(slotId, stack);
    }

    @Override
    public int getMaxStackSize(int slotId) {

        return this.container.getMaxStackSize();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {

        if (this.container instanceof WorldlyContainer worldly) {

            return worldly.getSlotsForFace(side);
        }

        return IntStream.range(0, this.getSize()).toArray();
    }

    @Override
    public boolean canInsert(int slotId, ItemStack toInsert, Direction side) {

        if (this.container instanceof WorldlyContainer worldly) {

            return worldly.canPlaceItemThroughFace(slotId, toInsert, side);
        }

        return this.canInsert(slotId, toInsert);
    }

    @Override
    public boolean canExtract(int slotId, int toExtract, Direction side) {

        if (toExtract < 1) {

        }

        if (this.container instanceof WorldlyContainer worldly) {

            //return worldly.canTakeItemThroughFace(slotId, toExtract, side);
        }

        // Vanilla seems to have no mechanism to restrict extracting?
        return true;
    }
}

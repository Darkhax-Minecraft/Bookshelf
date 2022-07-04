package net.darkhax.bookshelf.api.inventory;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.stream.IntStream;

public class ContainerInventoryAccess<T extends Container> implements IInventoryAccess {

    protected final T internal;
    private final CachedSupplier<int[]> availableSlots;

    public ContainerInventoryAccess(T internal) {

        this.internal = internal;
        this.availableSlots = CachedSupplier.cache(() -> IntStream.range(0, internal.getContainerSize()).toArray());
    }

    @Override
    public int[] getAvailableSlots(Direction side) {

        return this.availableSlots.get();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return this.internal.getItem(slot);
    }

    @Override
    public ItemStack insert(int slot, ItemStack insertStack, Direction side, boolean modify) {

        return this.insert(slot, insertStack, side, modify, false);
    }

    public ItemStack insert(int slot, ItemStack insertStack, Direction side, boolean modify, boolean forceInsert) {

        if (insertStack.isEmpty() || (!forceInsert && !this.isValidForSlot(slot, insertStack, side))) {

            return insertStack;
        }

        final ItemStack existingStack = internal.getItem(slot);

        if (!existingStack.isEmpty()) {

            // Existing slot is already full. Insertion can't be completed.
            if (existingStack.getCount() >= Math.min(existingStack.getMaxStackSize(), getSlotSize(slot))) {

                return insertStack;
            }

            // The existing stack is not compatible with the inserted stack.
            if (!Services.INVENTORY_HELPER.canItemsStack(existingStack, insertStack)) {

                return insertStack;
            }

            // The amount of space we can fill.
            final int availableSpace = Math.min(insertStack.getMaxStackSize(), getSlotSize(slot) - existingStack.getCount());

            // No space available
            if (availableSpace < 1) {

                return insertStack;
            }

            // All the inserted items can fit.
            if (insertStack.getCount() <= availableSpace) {

                if (modify) {

                    final ItemStack replacement = insertStack.copy();
                    replacement.grow(existingStack.getCount());
                    this.updateSlot(slot, replacement);
                }

                return ItemStack.EMPTY;
            }

            // The stack can only be partially inserted.
            else {

                final ItemStack uninserted = insertStack.copy();

                if (modify) {

                    final ItemStack replacement = uninserted.split(availableSpace);
                    replacement.grow(existingStack.getCount());
                    this.updateSlot(slot, replacement);
                    return uninserted;
                }

                uninserted.shrink(availableSpace);
                return uninserted;
            }
        }

        // The slot is empty.
        else {

            final int availableSpace = Math.min(insertStack.getMaxStackSize(), getSlotSize(slot));

            // The slot doesn't have enough room.
            if (availableSpace < insertStack.getCount()) {

                final ItemStack uninserted = insertStack.copy();

                if (modify) {

                    this.updateSlot(slot, uninserted.split(availableSpace));
                    return uninserted;
                }

                uninserted.shrink(availableSpace);
                return uninserted;
            }

            // The slot can accept the full stack.
            else {

                if (modify) {

                    this.updateSlot(slot, insertStack.copy());
                }

                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack extract(int slot, int amount, Direction side, boolean modify) {

        if (amount == 0) {

            return ItemStack.EMPTY;
        }

        final ItemStack existingStack = internal.getItem(slot);

        if (existingStack.isEmpty()) {

            return ItemStack.EMPTY;
        }

        if (modify) {

            final int amountExtracted = Math.min(existingStack.getCount(), amount);
            final ItemStack extracted = this.internal.removeItem(slot, amountExtracted);
            this.internal.setChanged();
            return extracted;
        }

        else {

            final ItemStack extracted = existingStack.copy();

            if (amount < extracted.getCount())  {

                extracted.setCount(amount);
            }

            return extracted;
        }
    }

    @Override
    public int getSlotSize(int slot) {

        return this.internal.getMaxStackSize();
    }

    @Override
    public boolean isValidForSlot(int slot, ItemStack stack, Direction side) {

        return this.internal.canPlaceItem(slot, stack);
    }

    protected void updateSlot(int slot, ItemStack stack) {

        this.internal.setItem(slot, stack);
        this.internal.setChanged();
    }
}
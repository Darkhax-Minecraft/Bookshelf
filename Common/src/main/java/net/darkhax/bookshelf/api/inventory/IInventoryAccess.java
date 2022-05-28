package net.darkhax.bookshelf.api.inventory;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.IntConsumer;

/**
 * This interface provides a loader neutral way to interact with different types of inventories.
 * <p>
 * TODO mention getter
 */
public interface IInventoryAccess {

    /**
     * Gets an array of accessible slot IDs for the inventory.
     *
     * @return An array of accessible slot IDs.
     */
    default int[] getAvailableSlots() {

        return this.getAvailableSlots(null);
    }

    /**
     * Gets an array of accessible slot IDs for a given side of the inventory.
     *
     * @param side The side of the inventory being accessed. This is only relevant when accessing directional
     *             inventories.
     * @return An array of accessible slot IDs.
     */
    int[] getAvailableSlots(@Nullable Direction side);

    /**
     * Gets the ItemStack in a given slot within the inventory. The resulting ItemStack should be considered immutable.
     *
     * @param slot The ID of the slot to access.
     * @return The ItemStack in the given slot. This should be considered immutable.
     */
    ItemStack getStackInSlot(int slot);

    /**
     * Checks if an ItemStack can be inserted into the given slot.
     *
     * @param slot  The ID of the slot to insert into.
     * @param stack The ItemStack to insert.
     * @return Whether the item can be inserted or not.
     */
    default boolean canInsert(int slot, ItemStack stack) {

        return this.canInsert(slot, stack, null);
    }

    /**
     * Checks if an ItemStack can be inserted into the given slot.
     *
     * @param slot  The ID of the slot to insert into.
     * @param stack The ItemStack to insert.
     * @param side  The side of the inventory the item is being inserted through.
     * @return Whether the item can be inserted or not.
     */
    default boolean canInsert(int slot, ItemStack stack, @Nullable  Direction side) {

        final ItemStack remaining = this.insert(slot, stack, side, false);
        return remaining.getCount() < stack.getCount();
    }

    /**
     * Attempts to insert an ItemStack into the given slot. If the ItemStack will not fully fit a partial insertion will
     * be done instead.
     *
     * @param slot  The ID of the slot to insert into.
     * @param stack The ItemStack to insert.
     * @return The remaining ItemStack that was not inserted. An empty stack indicates that the item was fully inserted.
     */
    default ItemStack insert(int slot, ItemStack stack) {

        return insert(slot, stack, null);
    }

    /**
     * Attempts to insert an ItemStack into the given slot. If the ItemStack will not fully fit a partial insertion will
     * be done instead.
     *
     * @param slot  The ID of the slot to insert into.
     * @param stack The ItemStack to insert.
     * @param side  The side of the inventory the item is being inserted through.
     * @return The remaining ItemStack that was not inserted. An empty stack indicates that the item was fully inserted.
     */
    default ItemStack insert(int slot, ItemStack stack, @Nullable Direction side) {

        return insert(slot, stack, side, true);
    }

    /**
     * Attempts to insert an ItemStack into the given slot. If the ItemStack will not fully fit a partial insertion will
     * be done instead.
     *
     * @param slot   The ID of the slot to insert into.
     * @param stack  The ItemStack to insert.
     * @param side   The side of the inventory the item is being inserted through.
     * @param modify Should the contents of the inventory actually be modified?
     * @return The remaining ItemStack that was not inserted. An empty stack indicates that the item was fully inserted.
     */
    ItemStack insert(int slot, ItemStack stack, @Nullable Direction side, boolean modify);

    /**
     * Checks if items can be extracted from the specified slot of the inventory.
     *
     * @param slot   The ID of the slot to extract from.
     * @param amount The amount of items to extract.
     * @return Whether the items could be extracted or not.
     */
    default boolean canExtract(int slot, int amount) {

        return this.canExtract(slot, amount, null);
    }

    /**
     * Checks if items can be extracted from the specified slot of the inventory.
     *
     * @param slot   The ID of the slot to extract from.
     * @param amount The amount of items to extract.
     * @param side   The side the items are extracted from.
     * @return Whether the items could be extracted or not.
     */
    default boolean canExtract(int slot, int amount, @Nullable Direction side) {

        return !extract(slot, amount, side, false).isEmpty();
    }

    /**
     * Attempts to extract items from the specified amount. You are not guaranteed to receive an item or as many of the
     * item as you requested.
     *
     * @param slot   The ID of the slot to extract from.
     * @param amount The amount of items to extract.
     * @return The extracted items.
     */
    default ItemStack extract(int slot, int amount) {

        return extract(slot, amount, null);
    }

    /**
     * Attempts to extract items from the specified amount. You are not guaranteed to receive an item or as many of the
     * item as you requested.
     *
     * @param slot   The ID of the slot to extract from.
     * @param amount The amount of items to extract.
     * @param side   The side the items are extracted from.
     * @return The extracted items.
     */
    default ItemStack extract(int slot, int amount, @Nullable Direction side) {

        return extract(slot, amount, side, true);
    }

    /**
     * Attempts to extract items from the specified amount. You are not guaranteed to receive an item or as many of the
     * item as you requested.
     *
     * @param slot   The ID of the slot to extract from.
     * @param amount The amount of items to extract.
     * @param side   The side the items are extracted from.
     * @param modify Should the contents of the inventory actually be modified?
     * @return The extracted items.
     */
    ItemStack extract(int slot, int amount, @Nullable Direction side, boolean modify);

    /**
     * Get the amount of items that can be held by a given slot.
     *
     * @param slot The ID of the slot to query.
     * @return The amount of items that can be held by the slot.
     */
    int getSlotSize(int slot);

    /**
     * Checks if an ItemStack is the right type of item for a slot. This only validates the item and not the state of
     * the inventory itself. For example a brewing stand fuel slot will return true on blaze powder even if the slot is
     * full.
     * <p>
     * This check is performed automatically when inserting an item.
     *
     * @param slot  The ID of the slot to query.
     * @param stack The stack to test.
     * @return Whether the item is valid for the slot or not.
     */
    default boolean isValidForSlot(int slot, ItemStack stack) {

        return this.isValidForSlot(slot, stack, null);
    }

    /**
     * Checks if an ItemStack is the right type of item for a slot. This only validates the item and not the state of
     * the inventory itself. For example a brewing stand fuel slot will return true on blaze powder even if the slot is
     * full.
     * <p>
     * This check is performed automatically when inserting an item.
     *
     * @param slot  The ID of the slot to query.
     * @param stack The stack to test.
     * @param side  The side of the inventory being accessed.
     * @return Whether the item is valid for the slot or not.
     */
    boolean isValidForSlot(int slot, ItemStack stack, @Nullable Direction side);

    default void forEachSlot(IntConsumer slotAction, @Nullable Direction side) {

        for (int slotId : this.getAvailableSlots(side)) {

            slotAction.accept(slotId);
        }
    }
}

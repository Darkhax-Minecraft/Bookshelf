package net.darkhax.bookshelf.common.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IGameplayHelper {

    RandomSource RNG = RandomSource.create();

    /**
     * Gets the crafting remainder for a given item. This is required as some platforms have different logic for
     * determining the crafting remainder.
     *
     * @param input The input item.
     * @return The crafting remainder, or empty if none.
     */
    default ItemStack getCraftingRemainder(ItemStack input) {
        final ItemStackTemplate remainder = input.getItem().getCraftingRemainder();
        return remainder != null ? remainder.create() : ItemStack.EMPTY;
    }

    /**
     * If an inventory exists at the specified position, attempt to insert the item into all available slots until the
     * item has been fully inserted or no more slots are available.
     *
     * @param level The world instance.
     * @param pos   The position of the block.
     * @param side  The side you are accessing the inventory from. This is from the perspective of the inventory, not
     *              your block. For example a hopper on top of a chest is inserting downwards but would use the upwards
     *              face because that is the side of the chest being accessed.
     * @param stack The item to try inserting.
     * @return The remaining items that were not inserted.
     */
    default ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        if (stack.isEmpty()) {
            return stack;
        }
        final Container container = getContainer(level, pos);
        return container != null ? HopperBlockEntity.addItem(null, container, stack, side) : stack;
    }

    /**
     * Gets a vanilla container for a given position. This method supports block based containers like the composter,
     * and block entity based containers like a chest or barrel.
     *
     * @param level The world instance.
     * @param pos   The position to check.
     * @return The container that was found, or null if no container exists.
     */
    @Nullable
    default Container getContainer(ServerLevel level, BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof WorldlyContainerHolder holder) {
            return holder.getContainer(state, level, pos);
        }
        final BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof Container beContainer) {
            return beContainer;
        }
        return null;
    }

    /**
     * Attempts to add an item to a list based inventory. This code will try to insert into all available slots until
     * the item has been completely inserted or no items remain.
     *
     * @param stack     The item to add into the inventory.
     * @param inventory The list of items to add to.
     * @param slots     An array of valid slots to add to.
     * @return The remaining items that were not inserted.
     */
    default ItemStack addItem(ItemStack stack, NonNullList<ItemStack> inventory, int[] slots) {
        for (int slot : slots) {
            if (stack.isEmpty()) {
                return stack;
            }
            final ItemStack existing = inventory.get(slot);
            if (existing.isEmpty()) {
                inventory.set(slot, stack);
                return ItemStack.EMPTY;
            }
            else if (existing.getCount() < existing.getMaxStackSize() && ItemStack.isSameItemSameComponents(existing, stack)) {
                final int availableSpace = existing.getMaxStackSize() - existing.getCount();
                final int movedAmount = Math.min(stack.getCount(), availableSpace);
                stack.shrink(movedAmount);
                existing.grow(movedAmount);
            }
        }
        return stack;
    }

    /**
     * Drops the crafting remainder of an item into the world if the item has one.
     *
     * @param level The world to drop the item within.
     * @param pos   The position to spawn the items at.
     * @param old   The base item to spawn a remainder from.
     */
    default void dropRemainders(Level level, BlockPos pos, ItemStack old) {
        if (!level.isClientSide() && !old.isEmpty()) {
            final ItemStack remainder = this.getCraftingRemainder(old);
            if (!remainder.isEmpty()) {
                Block.popResource(level, pos, remainder.copy());
            }
        }
    }

    CreativeModeTab.Builder tabBuilder();
}
package net.darkhax.bookshelf.common.api.util;

import net.darkhax.bookshelf.common.api.registry.register.RegisterMenuScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

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
        if (input.getItem().hasCraftingRemainingItem()) {
            final Item remainder = input.getItem().getCraftingRemainingItem();
            if (remainder != null) {
                return remainder.getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
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
     * Creates a new block entity builder using platform specific code. This is required because the underlying block
     * entity factory is not accessible.
     *
     * @param factory     A factory that creates a new block entity instance.
     * @param validBlocks The array of valid blocks for the block entity.
     * @param <T>         The type of the block entity.
     * @return A new builder for your block entity type.
     */
    <T extends BlockEntity> BlockEntityType.Builder<T> blockEntityBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks);


    /**
     * Binds a screen to a menu using platform specific code. This is required because the underlying screen factory is
     * not accessible.
     *
     * @param type    The menu type to bind the screen to.
     * @param factory A factory that constructs the screen instance.
     * @param <M>     The type of the menu.
     * @param <U>     The type of the screen.
     */
    <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindMenu(MenuType<? extends M> type, RegisterMenuScreen.ScreenFactory<M, U> factory);
}
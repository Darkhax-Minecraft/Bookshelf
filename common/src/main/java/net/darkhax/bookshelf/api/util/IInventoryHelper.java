package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.inventory.ContainerInventoryAccess;
import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
import net.darkhax.bookshelf.api.inventory.WorldlyContainerInventoryAccess;
import net.darkhax.bookshelf.mixin.accessors.inventory.AccessorCraftingMenu;
import net.darkhax.bookshelf.mixin.accessors.inventory.AccessorInventoryMenu;
import net.darkhax.bookshelf.mixin.accessors.inventory.AccessorTransientCraftingContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IInventoryHelper {

    /**
     * Gets the internal menu context that is backing a crafting container.
     *
     * @param container The crafting container to retrieve internal menu context from.
     * @return The internal menu, or null if the menu could not be found.
     */
    @Nullable
    default AbstractContainerMenu getCraftingContainer(CraftingContainer container) {

        return (container instanceof AccessorTransientCraftingContainer accessor) ? accessor.bookshelf$getMenu() : null;
    }

    @Nullable
    default Player getCraftingPlayer(Container container) {

        if (container instanceof Inventory playerInv) {

            return playerInv.player;
        }

        else if (container instanceof CraftingContainer crafting) {

            final AbstractContainerMenu menu = getCraftingContainer(crafting);

            if (menu instanceof AccessorCraftingMenu accessor) {

                return accessor.bookshelf$getPlayer();
            }

            else if (menu instanceof AccessorInventoryMenu accessor) {

                return accessor.bookshelf$getOwner();
            }
        }

        // TODO add some way for dynamic container resolution?
        return null;
    }

    /**
     * Damages an ItemStack by a set amount.
     * <p>
     * ItemStacks with the Unbreakable tag are treated as immune to stack damage as per Minecraft's base spec.
     * <p>
     * ItemStacks with the unbreaking enchantment will have a chance of ignoring the damage.
     * <p>
     * ItemStacks that do not have durability will not be modified.
     * <p>
     * Players in Creative Mode can not damage ItemStacks.
     *
     * @param stack  The ItemStack to damage.
     * @param amount The amount of damage to apply to the item.
     * @param owner  The entity that owns the ItemStack. This is optional but will be used for certain events.
     * @param slot   The slot the ItemStack is in. This is optional and used for the item break animation.
     */
    default ItemStack damageStack(ItemStack stack, int amount, @Nullable LivingEntity owner, @Nullable EquipmentSlot slot) {

        // Only items with durability can be damaged. Items with an Unbreakable tag override damage.
        if (stack.isDamageableItem()) {

            final RandomSource random = owner != null ? owner.getRandom() : Constants.RANDOM_SOURCE;

            // If an owner is present, use the entity aware version.
            if (owner != null) {

                stack.hurtAndBreak(amount, owner, e -> {

                    if (slot != null) {
                        e.broadcastBreakEvent(slot);
                    }
                });
            }

            // Try to damage the stack directly.
            else if (stack.hurt(amount, random, null)) {

                // Destroy the ItemStack when it has no more durability.
                stack.shrink(1);
                stack.setDamageValue(0);
            }
        }

        return stack;
    }

    default NonNullList<ItemStack> keepDamageableItems(CraftingContainer inv, NonNullList<ItemStack> keptItems, int damageAmount) {

        @Nullable
        final Player player = this.getCraftingPlayer(inv);

        for (int i = 0; i < keptItems.size(); i++) {

            final ItemStack input = inv.getItem(i).copy();

            if (input.getItem().canBeDepleted() || (input.hasTag() && input.getTag().getBoolean("Unbreaking"))) {

                final ItemStack stack = this.damageStack(input, damageAmount, player, null);

                if (!stack.isEmpty()) {

                    keptItems.set(i, stack);
                }
            }
        }

        return keptItems;
    }

    /**
     * Creates a list view of a Container's inventory contents.
     *
     * @param inventory The container to view.
     * @return A list view of the provided containers inventory contents.
     */
    default NonNullList<ItemStack> toList(Container inventory) {

        final NonNullList<ItemStack> items = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        applyForEach(inventory, items::set);
        return items;
    }

    /**
     * Fills an inventory with a list of ItemStack. The inventory size and container size must be identical.
     *
     * @param inventory The inventory to fill.
     * @param fillWith  The items to fill the inventory with.
     */
    default void fill(Container inventory, NonNullList<ItemStack> fillWith) {

        if (inventory.getContainerSize() != fillWith.size()) {

            throw new IllegalStateException("Inventory size did not match! inv_size=" + inventory.getContainerSize() + " fill_size=" + fillWith.size());
        }

        for (int slotId = 0; slotId < fillWith.size(); slotId++) {

            inventory.setItem(slotId, fillWith.get(slotId));
        }
    }

    /**
     * Applies an action to every item within the inventory.
     *
     * @param inventory The inventory container.
     * @param action    The action to apply.
     */
    default void applyForEach(Container inventory, Consumer<ItemStack> action) {

        for (int slotId = 0; slotId < inventory.getContainerSize(); slotId++) {

            action.accept(inventory.getItem(slotId));
        }
    }

    /**
     * Applies an action to every item within the inventory.
     *
     * @param inventory The inventory container.
     * @param action    The action to apply.
     */
    default void applyForEach(Container inventory, BiConsumer<Integer, ItemStack> action) {

        for (int slotId = 0; slotId < inventory.getContainerSize(); slotId++) {

            action.accept(slotId, inventory.getItem(slotId));
        }
    }

    /**
     * Checks if two ItemStack may be stacked together.
     *
     * @param original The original stack.
     * @param toStack  The stack attempting to be stacked into the original.
     * @return Can the two stacks be stacked together.
     */
    default boolean canItemsStack(ItemStack original, ItemStack toStack) {

        return !toStack.isEmpty() && ItemStack.isSameItem(original, toStack) && original.hasTag() == toStack.hasTag() && (!original.hasTag() || original.getTag().equals(toStack.getTag()));
    }

    /**
     * Attempts to provide access to an inventory stored at the given position.
     *
     * @param level     The world to query.
     * @param pos       The position to look at.
     * @param direction The side of the inventory being accessed.
     * @return Access to the inventory at the given position.
     */
    @Nullable
    default IInventoryAccess getInventory(Level level, BlockPos pos, @Nullable Direction direction) {

        final BlockState state = level.getBlockState(pos);

        // Handle blocks like the composter that think they're inventories.
        if (state.getBlock() instanceof WorldlyContainerHolder holder) {

            final WorldlyContainer container = holder.getContainer(state, level, pos);

            if (container != null) {

                return new ContainerInventoryAccess<>(container);
            }
        }

        // Handle vanilla tile entity containers
        else {

            final BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof Container container) {

                if (container instanceof WorldlyContainer worldly) {

                    return new WorldlyContainerInventoryAccess<>(worldly);
                }

                return new ContainerInventoryAccess<>(container);
            }
        }

        return null;
    }

    default void openMenu(ServerPlayer player, MenuProvider provider) {

        this.openMenu(player, provider, buf -> {});
    }

    default void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> buf) {

        this.openMenu(player, provider, buf, false);
    }

    void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> buf, boolean allowFakes);

    default ItemStack getCraftingRemainder(ItemStack stack) {

        return this.hasCraftingRemainder(stack) ? new ItemStack(stack.getItem().getCraftingRemainingItem()) : ItemStack.EMPTY;
    }

    default boolean hasCraftingRemainder(ItemStack stack) {

        return stack.getItem().hasCraftingRemainingItem();
    }

    boolean isFakePlayer(Player player);
}
package net.darkhax.bookshelf.common.api.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * A basic output slot implementation.
 */
public class OutputSlot extends Slot {

    @Nullable
    private final BiConsumer<Player, ItemStack> takeFunc;

    public OutputSlot(Container potContainer, int slot, int x, int y) {
        this(potContainer, slot, x, y, null);
    }

    public OutputSlot(Container potContainer, int slot, int x, int y, @Nullable BiConsumer<Player, ItemStack> takeFunc) {
        super(potContainer, slot, x, y);
        this.takeFunc = takeFunc;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        if (this.takeFunc != null) {
            this.takeFunc.accept(player, stack);
        }
    }
}
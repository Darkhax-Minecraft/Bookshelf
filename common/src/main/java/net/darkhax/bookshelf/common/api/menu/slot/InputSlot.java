package net.darkhax.bookshelf.common.api.menu.slot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * A basic input slot implementation.
 */
public class InputSlot extends Slot {

    private final Identifier emptyTexture;
    private final Predicate<ItemStack> canPlace;

    public InputSlot(Container container, int slot, int x, int y, Identifier emptyTexture) {
        this(container, slot, x, y, emptyTexture, stack -> true);
    }

    public InputSlot(Container container, int slot, int x, int y, Identifier emptyTexture, Predicate<ItemStack> canPlace) {
        super(container, slot, x, y);
        this.emptyTexture = emptyTexture;
        this.canPlace = canPlace;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Identifier getNoItemIcon() {
        return this.emptyTexture;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.canPlace.test(stack);
    }
}
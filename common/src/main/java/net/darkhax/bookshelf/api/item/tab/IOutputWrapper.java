package net.darkhax.bookshelf.api.item.tab;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Supplier;

public interface IOutputWrapper {

    void accept(ItemStack stack);

    void accept(ItemLike item);

    default void accept(Supplier<Item> item) {

        accept(item.get());
    }

    default void acceptStacks(Collection<ItemStack> stacks) {

        stacks.forEach(this::accept);
    }

    default void acceptItems(Collection<ItemLike> items) {

        items.forEach(this::accept);
    }

    default void acceptItemIter(Iterable<Item> items) {

        items.forEach(this::accept);
    }

    default void acceptItemSuppliers(Collection<Supplier<Item>> items) {

        items.forEach(this::accept);
    }
}

package net.darkhax.bookshelf.api.item;

import net.darkhax.bookshelf.api.util.ItemStackHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

public class ItemStackBuilder {

    private ItemStack currentStack;

    public ItemStackBuilder(ItemLike item) {
        this(item, 1);
    }

    public ItemStackBuilder(Holder<Item> holder) {
        this(holder.value(), 1);
    }

    public ItemStackBuilder(Holder<Item> holder, int count) {
        this(holder.value(), count);
    }

    public ItemStackBuilder(ItemLike item, int count) {

        this(new ItemStack(item, count));
    }

    public ItemStackBuilder(ItemStack stack) {

        this.currentStack = stack;
    }

    public ItemStackBuilder name(Component name) {

        this.currentStack.setHoverName(name);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchantment) {

        return enchant(enchantment, 1);
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {

        this.currentStack.enchant(enchantment, level);
        return this;
    }

    public ItemStackBuilder lore(Component... lines) {

        ItemStackHelper.setLore(this.currentStack, lines);
        return this;
    }

    public ItemStackBuilder addLore(Component... lines) {

        ItemStackHelper.appendLore(this.currentStack, lines);
        return this;
    }

    public ItemStack build() {

        return this.currentStack.copy();
    }
}
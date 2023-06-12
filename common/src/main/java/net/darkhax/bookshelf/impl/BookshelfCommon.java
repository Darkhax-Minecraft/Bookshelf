package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.enchantment.BaseEnchantment;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.util.EnchantmentHelper;
import net.darkhax.bookshelf.impl.fixes.MC151457;
import net.minecraft.world.item.ItemStack;

public class BookshelfCommon {

    public BookshelfCommon() {

        Services.REGISTRIES.loadContent(new BookshelfContentProvider());
        MC151457.applyFix();
        Services.EVENTS.addItemAttributeListener(this::applyEnchantmentAttributes);
    }

    /**
     * Bookshelf provides a BaseEnchantment class which allows enchantments to easily define attributes that they apply
     * to items. We initialize a single event listener to handle this functionality to reduce unnecessary overhead.
     */
    private void applyEnchantmentAttributes(IItemAttributeEvent event) {
        final ItemStack stack = event.getItemStack();
        if (stack.isEnchanted()) {
            EnchantmentHelper.processEnchantments(stack, (ench, level) -> {
                if (ench instanceof BaseEnchantment base) {
                    base.applyModifiers(stack, level, event.getSlotType(), event::addModifier);
                }
            });
        }
    }
}
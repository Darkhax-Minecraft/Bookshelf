package net.darkhax.bookshelf.client.renderer;

import net.minecraft.item.ItemStack;

public interface IItemRenderer {
    void renderItemstack(ItemStack stack, BookshelfClientRegistry.EnumState preRender);
}

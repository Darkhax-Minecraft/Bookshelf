package net.darkhax.bookshelf.client.renderer;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IItemRenderer {
    void renderItemstack(ItemStack stack, IBakedModel model, BookshelfRegistry.EnumState state);
}

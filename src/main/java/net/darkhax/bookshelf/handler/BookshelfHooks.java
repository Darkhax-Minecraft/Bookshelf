package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.client.RenderRegistry;
import net.darkhax.bookshelf.client.renderer.IItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

public class BookshelfHooks {
    
    public static void onRenderItem (ItemStack stack, IBakedModel model) {
        
        for (IItemRenderer render : RenderRegistry.itemRenderList.get(stack.getItem()))
            render.renderItem(stack, model);
            
        GlStateManager.color(1f, 0f, 1f);
    }
}

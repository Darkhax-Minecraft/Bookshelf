package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.events.RenderItemEvent;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookshelfHooks {
    
    @SideOnly(Side.CLIENT)
    public static boolean renderItem (RenderItem renderer, ItemStack stack, IBakedModel model) {
        
        final RenderItemEvent.Allow allow = new RenderItemEvent.Allow(renderer, stack, model);
        MinecraftForge.EVENT_BUS.post(allow);
        
        if (!allow.isCanceled() || stack == null)
            return false;
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Pre(renderer, stack, model));
        
        if (model.isBuiltInRenderer()) {
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(stack);
        }
        
        else {
            
            renderer.renderModel(model, stack);
            
            final RenderItemEvent.Glint event = new RenderItemEvent.Glint(renderer, stack, model);
            MinecraftForge.EVENT_BUS.post(event);
            
            if (event.isCanceled())
                RenderUtils.renderGlintEffect(renderer, stack, model, event.texture, event.getColor().getRGB());
            
            else if (stack.hasEffect())
                RenderUtils.renderGlintEffect(renderer, stack, model, RenderUtils.RES_ITEM_GLINT, -8372020);
        }
        
        MinecraftForge.EVENT_BUS.post(new RenderItemEvent.Post(renderer, stack, model));
        GlStateManager.popMatrix();
        
        return true;
    }
}

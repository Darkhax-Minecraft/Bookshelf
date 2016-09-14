package net.darkhax.bookshelf.handler;

import java.util.List;

import net.darkhax.bookshelf.client.RenderRegistry;
import net.darkhax.bookshelf.client.renderer.IItemRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookshelfHooks {
    
    /**
     * Called before an item is rendered.
     * 
     * @param stack The ItemStack being rendered.
     * @param model The model being rendered.
     */
    @SideOnly(Side.CLIENT)
    public static void renderPreModel (ItemStack stack, IBakedModel model) {
        
        renderItem(stack, model, EnumState.PRE_MODEL);
    }
    
    /**
     * Called after an item is rendered.
     * 
     * @param stack The ItemStack being rendered.
     * @param model The model being rendered.
     */
    @SideOnly(Side.CLIENT)
    public static void renderPostModel (ItemStack stack, IBakedModel model) {
        
        renderItem(stack, model, EnumState.POST_MODEL);
    }
    
    /**
     * Called before an item is rendered.
     * 
     * @param stack The ItemStack being rendered.
     * @param model The model being rendered.
     */
    @SideOnly(Side.CLIENT)
    public static void renderPostEffect (ItemStack stack, IBakedModel model) {
        
        renderItem(stack, model, EnumState.POST_EFFECTS);
    }
    
    @SideOnly(Side.CLIENT)
    public static void renderItem (ItemStack stack, IBakedModel model, EnumState state) {
        
        final List<IItemRenderer> itemRender = RenderRegistry.itemRenderList.get(stack.getItem());
        
        if (itemRender != null)
            for (final IItemRenderer itemRenderer : itemRender)
                itemRenderer.renderItem(stack, model, state);
    }
    
    @SideOnly(Side.CLIENT)
    public enum EnumState {
        
        PRE_MODEL,
        POST_MODEL,
        POST_EFFECTS
    }
}

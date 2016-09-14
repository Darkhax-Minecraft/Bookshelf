package net.darkhax.bookshelf.client.renderer;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IItemRenderer {
    
    /**
     * Called when an item that has been bound to the renderer is being rendered.
     * 
     * @param stack The ItemStack being rendered.
     * @param model The model being rendered.
     */
    void renderItem (ItemStack stack, IBakedModel model);
}

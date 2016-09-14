package net.darkhax.bookshelf.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.darkhax.bookshelf.client.renderer.IItemRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRegistry {
    
    /**
     * A map which all registered IItemRenderers to their items.
     */
    public static Map<Item, List<IItemRenderer>> itemRenderList = new HashMap<Item, List<IItemRenderer>>();
    
    /**
     * Binds an IItemRenderer to an Item. Items can have multiple IItemRenderers.
     * 
     * @param item The Item to bind the render to.
     * @param itemRenderer The Renderer to bind.
     */
    public static void bindItemRenderer (Item item, IItemRenderer itemRenderer) {
        
        List<IItemRenderer> rendererIntegerMap = itemRenderList.get(item);
        
        if (rendererIntegerMap == null) {
            
            rendererIntegerMap = new ArrayList<>();
            itemRenderList.put(item, rendererIntegerMap);
        }
        
        rendererIntegerMap.add(itemRenderer);
    }
}

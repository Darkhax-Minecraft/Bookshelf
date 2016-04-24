package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.block.BlockWoodenShelf;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        final Item item = Item.getItemFromBlock(Bookshelf.blockShelf);
        
        for (int meta = 0; meta < 5; meta++)
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("bookshelf:bookshelf_" + BlockWoodenShelf.types[meta], "inventory"));
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void postInit () {
    
    }
}

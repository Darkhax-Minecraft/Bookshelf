package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        for (final Feature feature : Bookshelf.features)
            feature.setupRendering();
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
    }
}
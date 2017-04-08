package net.darkhax.bookshelf;

import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.client.render.item.RenderFactoryItem;
import net.darkhax.bookshelf.entity.EntityFake;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.features.FeatureManager;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {

    @Override
    public void preInit () {

        for (final Feature feature : FeatureManager.getFeatures()) {
            feature.onClientPreInit();
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
        RenderingRegistry.registerEntityRenderingHandler(EntityFake.class, new RenderFactoryItem());
    }
}
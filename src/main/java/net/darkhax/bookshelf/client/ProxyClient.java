package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.client.render.item.RenderFactoryItem;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.entity.FakeEntity;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {

    @Override
    public void preInit () {

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
        RenderingRegistry.registerEntityRenderingHandler(FakeEntity.class, new RenderFactoryItem());
    }
}
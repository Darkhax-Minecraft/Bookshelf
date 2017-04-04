package net.darkhax.bookshelf.client;

import java.util.HashMap;
import java.util.Map;

import net.darkhax.bookshelf.client.model.ITileEntityRender;
import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.client.render.item.RenderFactoryItem;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.entity.FakeEntity;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {

    private static Map<Class<? extends TileEntity>, ITileEntityRender<?>> tileEntityRenderMap = new HashMap<>();

    @Override
    public void preInit () {

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
        RenderingRegistry.registerEntityRenderingHandler(FakeEntity.class, new RenderFactoryItem());
    }

    public static void registerTileEntityRender (Class<? extends TileEntity> tileEntity, ITileEntityRender<?> iTileEntityRender) {

        tileEntityRenderMap.put(tileEntity, iTileEntityRender);
    }

    public static ITileEntityRender<TileEntity> getTileEntityRender (TileEntity tileEntity) {

        return getTileEntityRender(tileEntity.getClass());
    }

    public static ITileEntityRender<TileEntity> getTileEntityRender (Class<? extends TileEntity> tileEntity) {

        return (ITileEntityRender<TileEntity>) tileEntityRenderMap.get(tileEntity);
    }

}
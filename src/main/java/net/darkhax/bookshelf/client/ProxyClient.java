package net.darkhax.bookshelf.client;

import java.lang.reflect.Field;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.client.render.item.RenderItemWrapper;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.entity.FakeEntity;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {
    
    @Override
    public void preInit () {
        
        for (final Feature feature : Bookshelf.features)
            feature.setupRendering();
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
        
        
        RenderingRegistry.registerEntityRenderingHandler(FakeEntity.class, manager -> {
            
            try {
                
                for (final Render<? extends Entity> render : manager.entityRenderMap.values())
                    if (render != null)
                        for (final Field field : render.getClass().getDeclaredFields())
                            if (field.getType().equals(RenderItem.class)) {
                                field.setAccessible(true);
                                field.set(render, RenderItemWrapper.instance());
                            }
            }
            
            catch (final Exception e) {
                
                throw new RuntimeException("Unable to reflect an EntityRenderer!", e);
            }
            
            return new Render<FakeEntity>(manager) {
                
                @Override
                protected ResourceLocation getEntityTexture (FakeEntity entity) {
                    
                    return null;
                }
            };
        });
    }
}
package net.darkhax.bookshelf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.client.render.item.RenderItemWrapper;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.entity.FakeEntity;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.features.attribcap.FeatureAttributeFix;
import net.darkhax.bookshelf.features.bookshelves.FeatureBookshelves;
import net.darkhax.bookshelf.features.playerheads.FeaturePlayerHeads;
import net.darkhax.bookshelf.features.supporters.FeatureSupporters;
import net.darkhax.bookshelf.handler.ConfigurationHandler;
import net.darkhax.bookshelf.handler.ForgeEventHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER)
public class Bookshelf {
    
    @SidedProxy(serverSide = Constants.PROXY_COMMON, clientSide = Constants.PROXY_CLIENT)
    public static ProxyCommon proxy;
    
    @Mod.Instance(Constants.MOD_ID)
    public static Bookshelf instance;
    
    /**
     * A list of feature objects. The feature system is specifically intended for features that
     * are bundled with the library and should not be considered part of the API/Library.
     */
    public static List<Feature> features = new ArrayList<>();
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        
        features.add(new FeatureBookshelves());
        features.add(new FeaturePlayerHeads());
        features.add(new FeatureSupporters());
        features.add(new FeatureAttributeFix());
        
        new ConfigurationHandler(event.getSuggestedConfigurationFile());
        
        for (final Feature feature : features)
            feature.onPreInit();
        
        GameRegistry.registerTileEntity(TileEntityBasicChest.class, "basic_chest");
        proxy.preInit();
        
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
    
    @EventHandler
    public void init (FMLInitializationEvent event) {
        
        RenderItemWrapper.instance();
    }
}
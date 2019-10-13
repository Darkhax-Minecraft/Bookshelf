package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class RegistryHelperClient extends RegistryHelper {
    
    public RegistryHelperClient(String modid, Logger logger, ItemGroup group) {
        
        super(modid, logger, group);
    }
    
    @Override
    public void initialize (IEventBus modBus) {
        
        super.initialize(modBus);
        modBus.addListener(this::onClientSetup);
    }
    
    private void onClientSetup (FMLClientSetupEvent event) {
        
        if (!this.tileEntityRenderers.isEmpty()) {
            
            this.registerTileEntityRenderers();
        }
        
        if (!this.entityRenderers.isEmpty()) {
            
            this.registerEntityRenderers();
        }
    }
    
    /**
     * TILE ENTITY RENDERERS
     */
    private final Map<Class<? extends TileEntity>, TileEntityRenderer<? super TileEntity>> tileEntityRenderers = new HashMap<>();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setSpecialRenderer (Class<? extends TileEntity> tileEntityClass, TileEntityRenderer specialRenderer) {
        
        this.tileEntityRenderers.put(tileEntityClass, specialRenderer);
    }
    
    private void registerTileEntityRenderers () {
        
        if (!this.tileEntityRenderers.isEmpty()) {
            
            this.logger.info("Registering {} TileEntity Renderers.", this.tileEntityRenderers.size());
            
            for (final Map.Entry<Class<? extends TileEntity>, TileEntityRenderer<? super TileEntity>> entry : this.tileEntityRenderers.entrySet()) {
                
                ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * GUI SCREENS
     */
    @SuppressWarnings("rawtypes")
    private final Map<ContainerType<? extends Container>, IScreenFactory> guis = new HashMap<>();
    
    public <M extends Container, U extends Screen & IHasContainer<M>> void registerGuiScreen (ContainerType<? extends M> containerType, IScreenFactory<M, U> factory) {
        
        this.guis.put(containerType, factory);
    }
    
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void registerContainerTypes (Register<ContainerType<?>> event) {
        
        super.registerContainerTypes(event);
        
        if (!this.guis.isEmpty()) {
            
            this.logger.info("Registering {} GUI screens.", this.guis.size());
            
            for (final Entry<ContainerType<? extends Container>, IScreenFactory> entry : this.guis.entrySet()) {
                
                ScreenManager.registerFactory(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * ENTITY RENDERERS
     */
    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Entity>, IRenderFactory> entityRenderers = new HashMap<>();
    
    @SuppressWarnings("rawtypes")
    public void registerEntityRenderer (Class<? extends Entity> clazz, IRenderFactory renderFactory) {
        
        this.entityRenderers.put(clazz, renderFactory);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerEntityRenderers () {
        
        this.logger.info("Registering {} entity renderers.", this.entityRenderers.size());
        
        for (final Entry<Class<? extends Entity>, IRenderFactory> entry : this.entityRenderers.entrySet()) {
            
            RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), entry.getValue());
        }
    }
}
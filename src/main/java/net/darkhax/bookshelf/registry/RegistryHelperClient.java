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
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class RegistryHelperClient extends RegistryHelper {
    
    public RegistryHelperClient(String modid, Logger logger, ItemGroup group) {
        
        super(modid, logger, group);
    }
    
    public void initialize (IEventBus modBus) {
        
        super.initialize(modBus);
        modBus.addListener(this::onClientSetup);
    }
    
    private void onClientSetup (FMLClientSetupEvent event) {
        
        this.registerTileEntityRenderers();
    }
    
    /**
     * TILE ENTITY RENDERERS
     */
    private Map<Class<? extends TileEntity>, TileEntityRenderer<? super TileEntity>> tileEntityRenderers = new HashMap<>();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setSpecialRenderer (Class<? extends TileEntity> tileEntityClass, TileEntityRenderer specialRenderer) {
        
        this.tileEntityRenderers.put(tileEntityClass, specialRenderer);
    }
    
    private void registerTileEntityRenderers () {
        
        if (!this.tileEntityRenderers.isEmpty()) {
            
            this.logger.info("Registering {} TileEntity Renderers.", this.tileEntityRenderers.size());
            
            for (Map.Entry<Class<? extends TileEntity>, TileEntityRenderer<? super TileEntity>> entry : tileEntityRenderers.entrySet()) {
                
                ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * GUI SCREENS
     */
    @SuppressWarnings("rawtypes")
    private Map<ContainerType<? extends Container>, IScreenFactory> guis = new HashMap<>();
    
    public <M extends Container, U extends Screen & IHasContainer<M>> void registerGuiScreen (ContainerType<? extends M> containerType, IScreenFactory<M, U> factory) {
        
        guis.put(containerType, factory);
    }
    
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void registerContainerTypes (Register<ContainerType<?>> event) {
        
        super.registerContainerTypes(event);
        
        if (!this.guis.isEmpty()) {
            
            this.logger.info("Registering {} GUI screens.", this.guis.size());
            
            for (Entry<ContainerType<? extends Container>, IScreenFactory> entry : guis.entrySet()) {
                
                ScreenManager.registerFactory(entry.getKey(), entry.getValue());
            }
        }
    }
}
package net.darkhax.bookshelf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {
    
    /**
     * Registers a LayerRenderer with a RendererLivingEntity. Only works for living entities.
     * 
     * @param entityClass The class of the Entity to register the renderer to.
     * @param layer The LayerRenderer to register to that entity.
     * @return boolean Whether or not the layer was succesfully registered.
     */
    public static boolean registerEntityLayer (Class<? extends Entity> entityClass, LayerRenderer layer) {
        
        RendererLivingEntity renderer = getEntityRenderer(entityClass);
        
        if (renderer != null) {
            
            renderer.addLayer(layer);
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a RendererLivingEntity instance from the render manager based on the provided
     * entity class. Only works for living entities.
     * 
     * @param entityClass The class of the Entity to register the renderer to.
     * @return
     */
    public static RendererLivingEntity getEntityRenderer (Class<? extends Entity> entityClass) {
        
        Render renderer = Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entityClass);
        return (renderer instanceof RendererLivingEntity) ? (RendererLivingEntity) renderer : null;
    }
    
    /**
     * Registers a LayerRenderer to both the Alex and Steve player renderers.
     * 
     * @param layer The LayerRenderer to register.
     */
    public static void registerPlayerLayer (LayerRenderer layer) {
        
        getSteveRenderer().addLayer(layer);
        getAlexRenderer().addLayer(layer);
    }
    
    /**
     * Gets the RenderPlayer instance for the Steve model.
     * 
     * @return RederPlayer The RenderPlayer instance for the Steve model.
     */
    public static RenderPlayer getSteveRenderer () {
        
        return getPlayerRenderer("default");
    }
    
    /**
     * Gets the RenderPlayer instance for the Alex model.
     * 
     * @return RenderPlayer The RenderPlayer instance for the Alex model.
     */
    public static RenderPlayer getAlexRenderer () {
        
        return getPlayerRenderer("slim");
    }
    
    /**
     * Gets a RenderPlayer instance for the specified model type. Currently only Alex (slim)
     * and Steve (default) are added by vanilla. Can be null if no renderer was found.
     * 
     * @param type The name of the model type to retrieve.
     * @return RenderPlayer The RenderPlayer instance for the specified model.
     */
    public static RenderPlayer getPlayerRenderer (String type) {
        
        return Minecraft.getMinecraft().getRenderManager().skinMap.get(type);
    }
    
    /**
     * Attempts to set the skin texture for a player to the resource location passed to it.
     * 
     * @param player The AbstractClientPlayer to set the texture to.
     * @param skin The ResourceLocation for the new skin texture.
     * @return boolean Whether or not the texture was set.
     */
    public static boolean setPlayerSkin (AbstractClientPlayer player, ResourceLocation skin) {
        
        if (player.hasPlayerInfo() && skin != null) {
            
            player.getPlayerInfo().locationSkin = skin;
            return true;
        }
        
        return false;
    }
    
    /**
     * Attempts to set the cape texture for a player to the resource location passed to it.
     * 
     * @param player The AbstractClientPlayer to set the texture to.
     * @param cape The ResourceLocation for the new cape texture.
     * @return boolean Whether or not the texture was set.
     */
    public static boolean setPlayerCape (AbstractClientPlayer player, ResourceLocation cape) {
        
        if (player.hasPlayerInfo() && cape != null) {
            
            player.getPlayerInfo().locationCape = cape;
            return true;
        }
        
        return false;
    }
}

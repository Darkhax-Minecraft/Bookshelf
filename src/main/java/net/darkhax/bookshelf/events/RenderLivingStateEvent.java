package net.darkhax.bookshelf.events;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLivingStateEvent extends Event {
    
    /**
     * The entity being rendered.
     */
    private final EntityLivingBase entity;
    
    /**
     * Instance of the renderer
     */
    private final RenderLivingBase<EntityLivingBase> renderer;
    
    /**
     * Method specifically for modifying the render pipeline of an entity render. This event is
     * fired from within the push/pop of RenderLivingBase#renderLivingAt(T, double, double,
     * double).
     * 
     * @param entity The entity being rendered.
     * @param renderer Instance of the renderer.
     */
    public RenderLivingStateEvent(EntityLivingBase entity, RenderLivingBase<EntityLivingBase> renderer) {
        
        this.entity = entity;
        this.renderer = renderer;
    }
    
    /**
     * Gets the entity being rendered.
     * 
     * @return The entiyt being rendered.
     */
    public EntityLivingBase getEntity () {
        
        return this.entity;
    }
    
    /**
     * Gets the renderer instance.
     * 
     * @return Instance of the renderer.
     */
    public RenderLivingBase<EntityLivingBase> getRenderer () {
        
        return this.renderer;
    }
}

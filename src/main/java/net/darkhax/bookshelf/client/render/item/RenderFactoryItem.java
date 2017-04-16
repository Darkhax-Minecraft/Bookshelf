/*******************************************************************************************************************
 * Copyright: covers1624
 *
 * License: Lesser General Public License 2.1
 *          https://github.com/TheCBProject/CodeChickenLib/blob/397135f68774e0661d23cb608c893274635d6d6d/LICENSEw
 *
 * Original: https://github.com/TheCBProject/CodeChickenLib/blob/6d2202b3328e564509371db283a40d4b4d752287/src/main/java/codechicken/lib/render/item/CCRenderItem.java
 *
 * Changes: - Reformatted to Bookshelf's code style and formatting.
 *          - Wrote complete Javadocs.
 *          - Renamed class to RenderItemWrapper.
 *          - Renamed/Removed several fields and methods to better fit this project.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.client.render.item;

import java.lang.reflect.Field;

import net.darkhax.bookshelf.entity.EntityFake;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * A fake render factory, used to iterate through all other renderers, and patch their
 * reference to the global RenderItem class. For internal use only!
 */
public class RenderFactoryItem implements IRenderFactory<EntityFake> {
    
    @Override
    public Render<EntityFake> createRenderFor (RenderManager manager) {
        
        for (final Render<? extends Entity> render : manager.entityRenderMap.values())
            if (render != null) {
                this.patchRenderer(render);
            }
        
        return new Render<EntityFake>(manager) {
            
            @Override
            protected ResourceLocation getEntityTexture (EntityFake entity) {
                
                return null;
            }
        };
    }
    
    /**
     * Attempts to find any field which holds a RenderItem instance, and replaces it with the
     * RenderItemWrapper from this mod.
     *
     * @param render The render to patch.
     */
    private void patchRenderer (Render<? extends Entity> render) {
        
        try {
            
            for (final Field field : render.getClass().getDeclaredFields()) {
                if (field.getType().equals(RenderItem.class)) {
                    field.setAccessible(true);
                    field.set(render, RenderItemWrapper.instance());
                }
            }
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            
            Constants.LOG.warn("Could not patch renderer!", e);
        }
    }
}
package net.darkhax.bookshelf.item;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomMesh {

    /**
     * Gets a custom ItemMeshDefinition for the item. Keep in mind that the models still have
     * to be baked.
     *
     * @return
     */
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getCustomMesh ();

    /**
     * Provides a hook for new models to be baked.
     */
    @SideOnly(Side.CLIENT)
    public void registerMeshModels ();
}
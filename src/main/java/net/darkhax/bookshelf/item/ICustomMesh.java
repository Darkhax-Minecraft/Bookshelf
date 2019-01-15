/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface is used by bookshelf's registry to automatically detect an item that has
 * custom mesh definitions. This can be used to register your own model stuff.
 */
public interface ICustomMesh {
    
    /**
     * Gets a custom ItemMeshDefinition for the item. Keep in mind that the models still have
     * to be baked.
     *
     * @return
     */
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getCustomMesh ();
}
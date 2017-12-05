/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface lets an item register their own models.
 */
public interface ICustomModel {

    /**
     * Provides a hook for new models to be baked.
     */
    @SideOnly(Side.CLIENT)
    public void registerMeshModels ();
}

/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface is used by blocks to automatically register a TileEntity for a block.
 */
public interface ITileEntityProvider {

    /**
     * Gets the class of the tile entity to associate with the block.
     *
     * @return The TileEntity class to register associated to this block.
     */
    Class<? extends TileEntity> getTileEntityClass ();

    /**
     * Gets the TESR for the tile entity. If null is returned nothing will be done.
     *
     * @return The TESR to bind to the tile.
     */
    @SideOnly(Side.CLIENT)
    default TileEntitySpecialRenderer<?> getTileRenderer () {

        return null;
    }
}

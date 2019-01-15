/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface is used by the bookshelf registry system to automatically detect and register
 * item color handlers appropriately, without using proxies or any other complicated spread out
 * code.
 */
public interface IColorfulItem {
    
    /**
     * Gets the color handler for the item.
     *
     * @return The color handler for the item.
     */
    @SideOnly(Side.CLIENT)
    public IItemColor getColorHandler ();
}

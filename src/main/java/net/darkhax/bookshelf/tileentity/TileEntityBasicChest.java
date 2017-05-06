/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.tileentity;

import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;

/**
 * This class is simply an access point to bind the custom chest renderer. It doesn't add any
 * unique functionality beyond that.
 */
public class TileEntityBasicChest extends TileEntityChest {

    /**
     * Base constructor, used internally by mc/forge. Don't use this!
     */
    public TileEntityBasicChest () {

        super();
    }

    /**
     * Delegate constructor for the chest one.
     *
     * @param type The type of chest being created.
     */
    public TileEntityBasicChest (BlockChest.Type type) {

        super(type);
    }
}
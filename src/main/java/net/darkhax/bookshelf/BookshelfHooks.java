/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.lib.ModTrackingList;
import net.minecraft.item.crafting.CraftingManager;

public class BookshelfHooks {
    
    public static void onPrePreInit () {
        
        CraftingManager.getInstance().recipes = new ModTrackingList(CraftingManager.getInstance().recipes);
    }
}

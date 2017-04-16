/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public final class GuiUtils {
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private GuiUtils () {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Adds a standard player inventory to the container.
     *
     * @param container The container to add slots to.
     * @param player Player accessing the container.
     */
    public static void addPlayerInvToContainer (Container container, InventoryPlayer playerInv) {
        
        // Player Inventory
        for (int playerInvX = 0; playerInvX < 3; ++playerInvX) {
            for (int playerInvY = 0; playerInvY < 9; ++playerInvY) {
                container.addSlotToContainer(new Slot(playerInv, playerInvY + playerInvX * 9 + 9, 8 + playerInvY * 18, playerInvX * 18 + 51));
            }
        }
        
        // Hot Bar
        for (int hotbarIndex = 0; hotbarIndex < 9; ++hotbarIndex) {
            container.addSlotToContainer(new Slot(playerInv, hotbarIndex, 8 + hotbarIndex * 18, 109));
        }
    }
}

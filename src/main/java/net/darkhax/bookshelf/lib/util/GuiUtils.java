package net.darkhax.bookshelf.lib.util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiUtils {

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

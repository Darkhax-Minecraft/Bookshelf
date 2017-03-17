package net.darkhax.bookshelf.common;

import net.darkhax.bookshelf.lib.ModTrackingList;
import net.minecraft.item.crafting.CraftingManager;

public class BookshelfHooks {

    public static void onPrePreInit () {

        CraftingManager.getInstance().recipes = new ModTrackingList(CraftingManager.getInstance().recipes);
    }
}

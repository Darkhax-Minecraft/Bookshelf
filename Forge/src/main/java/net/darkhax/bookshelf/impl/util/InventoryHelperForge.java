package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.util.IInventoryHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;

public class InventoryHelperForge implements IInventoryHelper {

    @Override
    public Player getCraftingPlayer(Container container) {

        final Player player = ForgeHooks.getCraftingPlayer();
        return player != null ? player : IInventoryHelper.super.getCraftingPlayer(container);
    }
}
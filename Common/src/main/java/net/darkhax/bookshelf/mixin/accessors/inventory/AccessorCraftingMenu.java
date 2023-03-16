package net.darkhax.bookshelf.mixin.accessors.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingMenu.class)
public interface AccessorCraftingMenu {

    @Accessor("player")
    Player bookshelf$getPlayer();
}
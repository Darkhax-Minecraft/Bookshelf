package net.darkhax.bookshelf.mixin.accessors.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingContainer.class)
public interface AccessorCraftingContainer {

    @Accessor("menu")
    AbstractContainerMenu bookshelf$getMenu();
}
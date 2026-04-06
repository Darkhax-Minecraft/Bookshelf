package net.darkhax.bookshelf.common.mixin.access.level;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface AccessorRecipeManager {

    @Accessor("recipes")
    RecipeMap bookshelf$getRecipes();
}
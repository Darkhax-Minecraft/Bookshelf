package net.darkhax.bookshelf.mixin.accessors.world;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface AccessorRecipeManager {

    @Invoker("byType")
    Map bookshelf$getTypeMap(RecipeType type);
}
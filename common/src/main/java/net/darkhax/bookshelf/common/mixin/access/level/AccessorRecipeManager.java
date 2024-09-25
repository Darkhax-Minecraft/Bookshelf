package net.darkhax.bookshelf.common.mixin.access.level;

import com.google.common.collect.Multimap;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface AccessorRecipeManager {

    @Accessor("byType")
    Multimap<RecipeType<?>, RecipeHolder<?>> bookshelf$byTypeMap();
}
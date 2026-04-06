package net.darkhax.bookshelf.common.mixin.access.level;

import com.google.common.collect.Multimap;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeMap.class)
public interface AccessorRecipeMap {

    @Accessor("byType")
    Multimap<RecipeType<?>, RecipeHolder<?>> bookshelf$byType();
}

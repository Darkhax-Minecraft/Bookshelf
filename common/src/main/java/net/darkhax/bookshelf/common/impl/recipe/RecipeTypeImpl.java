package net.darkhax.bookshelf.common.impl.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public record RecipeTypeImpl<T extends Recipe<?>>(ResourceLocation id) implements RecipeType<T> {
    @Override
    public String toString() {
        return this.id.toString();
    }
}

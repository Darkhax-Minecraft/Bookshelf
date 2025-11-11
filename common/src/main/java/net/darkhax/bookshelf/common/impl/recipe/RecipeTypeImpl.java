package net.darkhax.bookshelf.common.impl.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public record RecipeTypeImpl<T extends Recipe<?>>(ResourceLocation id) implements RecipeType<T> {

    @NotNull
    @Override
    public String toString() {
        return this.id.toString();
    }
}

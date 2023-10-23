package net.darkhax.bookshelf.api.data.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

/**
 * A partial implementation of {@link Recipe} that provides the necessary ID handling and error detection.
 * <p>
 * Having a null {@link #getSerializer()} or {@link #getType()} is considered invalid and will raise a {@link
 * IllegalStateException} if either are detected as null.
 *
 * @param <C> The type of inventory used to craft this recipe.
 */
public abstract class RecipeBase<C extends Container> implements Recipe<C> {

    /**
     * The namespace identifier for the recipe entry.
     */
    private final ResourceLocation id;

    public RecipeBase(ResourceLocation id) {

        this.id = id;

        if (this.getSerializer() == null) {

            throw new IllegalStateException("Recipe class " + this.getClass().getName() + " can not be serialized.");
        }

        if (this.getType() == null) {

            throw new IllegalStateException("Recipe class " + this.getClass().getName() + " has no recipe type!");
        }
    }
}
package net.darkhax.bookshelf.api.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * This interface is a rough copy of {@link net.minecraft.world.item.crafting.RecipeSerializer}. Some platforms like
 * Forge modify the class hierarchy of the vanilla interface which creates compile time problems. This interface gets
 * around these issues by decoupling the neutral code. Platform specific wrappers are provided to bring this back to the
 * vanilla type.
 *
 * @param <T> The type of recipe serialized by the serializer.
 */
public abstract class IRecipeSerializer<T extends Recipe<?>> {

    private RecipeSerializer<?> wrapper;

    public void setWrapper(RecipeSerializer<?> wrapper) {

        this.wrapper = wrapper;
    }

    public RecipeSerializer<?> getWrapper() {

        return this.wrapper;
    }

    /**
     * Reads a recipe from JSON data.
     *
     * @param recipeId The ID to assign the recipe.
     * @param json     The JSON data.
     * @return The recipe that was deserialized.
     */
    public abstract T fromJson(ResourceLocation recipeId, JsonObject json);

    /**
     * Reads a recipe from a network buffer.
     *
     * @param recipeId The ID to assign the recipe.
     * @param buffer   The network buffer.
     * @return The recipe that was deserialized.
     */
    public abstract T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer);

    /**
     * Writes a recipe to a network buffer.
     *
     * @param buffer  The network buffer to write into.
     * @param toWrite The recipe to write.
     */
    public abstract void toNetwork(FriendlyByteBuf buffer, T toWrite);
}

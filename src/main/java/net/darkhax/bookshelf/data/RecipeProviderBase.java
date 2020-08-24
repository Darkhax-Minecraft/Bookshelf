package net.darkhax.bookshelf.data;

import java.nio.file.Path;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

/**
 * A layer on RecipeProvider which aims to improve the readability of your data generator code.
 * 
 * @param <T> The type of recipes to generate.
 */
public abstract class RecipeProviderBase<T extends IFinishedRecipe> extends RecipeProvider {
    
    @Nullable
    private Consumer<IFinishedRecipe> recipes;
    
    public RecipeProviderBase(DataGenerator generator) {
        
        super(generator);
    }
    
    @Override
    protected void registerRecipes (Consumer<IFinishedRecipe> recipes) {
        
        this.recipes = recipes;
        this.register(recipes);
        this.recipes = null;
    }
    
    @Override
    protected void saveRecipeAdvancement (DirectoryCache cache, JsonObject advancementJson, Path pathIn) {
        
        // Ignore advancements by default.
    }
    
    /**
     * Gets the recipe consumer. This is used to add a recipe to the data generator.
     * 
     * @return The recipe consumer used to add recipes to the generator.
     */
    public Consumer<IFinishedRecipe> getRecipeConsumer () {
        
        return this.recipes;
    }
    
    /**
     * Adds a new recipe to be generated.
     * 
     * @param recipe The recipe to be generated.
     */
    public void addRecipe (T recipe) {
        
        this.recipes.accept(recipe);
    }
    
    /**
     * Register all your recipes in here. The {@link #recipes} field will be bound allowing
     * {@link #getRecipeConsumer()} to be used.
     * 
     * @param recipes The normal recipe consumer. This will be the same value as
     *        {@link #getRecipeConsumer()}.
     */
    abstract void register (Consumer<IFinishedRecipe> recipes);
    
    /**
     * Gets the modid that owns the recipes being generated. Can be used by helper functions to
     * generate your recipes.
     * 
     * @return The modid that owns the recipe provider.
     */
    abstract String getModId ();
}
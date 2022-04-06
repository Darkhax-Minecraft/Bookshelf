package net.darkhax.bookshelf.mixin.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface AccessorShapedRecipe {

    @Invoker("keyFromJson")
    static Map<String, Ingredient> bookshelf$keyFromJson(JsonObject $$0) {

        throw new IllegalStateException("Mixin failed.");
    }

    @Invoker("shrink")
    static String[] bookshelf$shrink(String... pattern) {
        throw new IllegalStateException("Mixin failed.");
    }

    @Invoker("patternFromJson")
    static String[] bookshelf$patternFromJson(JsonArray json) {

        throw new IllegalStateException("Mixin failed.");
    }

    @Invoker("dissolvePattern")
    static NonNullList<Ingredient> bookshelf$dissolvePattern(String[] pattern, Map<String, Ingredient> ingredients, int width, int height) {

        throw new IllegalStateException("Mixin failed.");
    }
}

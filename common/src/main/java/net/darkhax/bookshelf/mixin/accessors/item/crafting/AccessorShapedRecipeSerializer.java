package net.darkhax.bookshelf.mixin.accessors.item.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShapedRecipe.Serializer.class)
public interface AccessorShapedRecipeSerializer {

    @Accessor("PATTERN_CODEC")
    static Codec<List<String>> bookshelf$getPatternCodec() {
        throw new AssertionError("Mixins failed to load.");
    }

    @Accessor("SINGLE_CHARACTER_STRING_CODEC")
    static Codec<String> bookshelf$getSingleCharacterStringCodec() {
        throw new AssertionError("Mixins failed to load.");
    }
}
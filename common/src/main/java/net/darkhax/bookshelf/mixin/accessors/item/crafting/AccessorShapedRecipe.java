package net.darkhax.bookshelf.mixin.accessors.item.crafting;

import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ShapedRecipe.class)
public interface AccessorShapedRecipe {

    @Invoker("shrink")
    static String[] bookshelf$shrink(List<String> pattern) {
        throw new IllegalStateException("Mixin failed.");
    }
}

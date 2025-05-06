package net.darkhax.bookshelf.common.mixin.patch.item;

import net.darkhax.bookshelf.common.api.registry.register.RegisterRecipeType;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.recipe.RecipeTypeImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeType.class)
public interface MixinRecipeType {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerRecipeTypes(new RegisterRecipeType(provider.contentNamespace(), id -> Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeTypeImpl<>(id)))));
    }
}

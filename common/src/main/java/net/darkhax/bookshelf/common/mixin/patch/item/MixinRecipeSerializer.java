package net.darkhax.bookshelf.common.mixin.patch.item;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeSerializer.class)
public interface MixinRecipeSerializer {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerRecipeSerializers(new Register<>(provider.contentNamespace(), (id, serializer) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer))));
    }
}
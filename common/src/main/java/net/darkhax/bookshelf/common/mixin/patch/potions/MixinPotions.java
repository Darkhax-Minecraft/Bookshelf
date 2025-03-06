package net.darkhax.bookshelf.common.mixin.patch.potions;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Potions.class)
public class MixinPotions {

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void onBootstrap(Registry<Potion> registry, CallbackInfoReturnable<Potion> cir) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerPotions(new Register<>(owner, (id, potionType) -> Registry.register(BuiltInRegistries.POTION, id, potionType)));
        });
    }
}
package net.darkhax.bookshelf.common.mixin.patch.advancement;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSubPredicates.class)
public class MixinItemSubPredicates {

    @Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/advancements/critereon/ItemSubPredicate$Type;", at = @At("RETURN"))
    private static void onBootstrap(Registry<ItemSubPredicate.Type<?>> registry, CallbackInfoReturnable<ItemSubPredicate.Type<?>> cir) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerItemSubPredicates(new Register<>(owner, (id, predicate) -> Registry.register(BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE, id, predicate)));
        });
    }
}
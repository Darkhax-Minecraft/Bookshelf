package net.darkhax.bookshelf.common.mixin.patch.advancement;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CriteriaTriggers.class)
public class MixinCriteriaTriggers {

    @Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/advancements/CriterionTrigger;", at = @At("RETURN"))
    private static void bootstrap(Registry<CriterionTrigger<?>> registry, CallbackInfoReturnable<CriterionTrigger<?>> cir) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerCriteriaTriggers(new Register<>(owner, (id, predicate) -> Registry.register(registry, id, predicate)));
        });
    }
}

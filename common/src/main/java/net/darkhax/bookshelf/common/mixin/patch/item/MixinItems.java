package net.darkhax.bookshelf.common.mixin.patch.item;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterItem;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class MixinItems {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            provider.registerItems(new Register<>(provider.contentNamespace(), Items::registerItem));
            provider.registerItems(new RegisterItem(provider.contentNamespace(), Items::registerItem));
        });
    }
}

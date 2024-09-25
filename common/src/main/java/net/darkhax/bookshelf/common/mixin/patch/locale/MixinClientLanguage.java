package net.darkhax.bookshelf.common.mixin.patch.locale;

import net.darkhax.bookshelf.common.impl.resources.ExtendedText;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLanguage.class)
public class MixinClientLanguage {

    @Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
    public void getOrDefault(String key, String fallback, CallbackInfoReturnable<String> cbi) {
        if (ExtendedText.INSTANCE.get().has(key)) {
            cbi.setReturnValue(ExtendedText.INSTANCE.get().get(key));
        }
    }

    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    public void has(String key, CallbackInfoReturnable<Boolean> cbi) {
        if (ExtendedText.INSTANCE.get().has(key)) {
            cbi.setReturnValue(true);
        }
    }
}
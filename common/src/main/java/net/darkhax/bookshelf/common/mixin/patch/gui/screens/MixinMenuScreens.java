package net.darkhax.bookshelf.common.mixin.patch.gui.screens;

import net.darkhax.bookshelf.common.api.registry.register.RegisterMenuScreen;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.gui.screens.MenuScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MenuScreens.class)
public class MixinMenuScreens {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo cbi) {
        final RegisterMenuScreen registry = new RegisterMenuScreen();
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerMenuScreens(registry));
    }
}
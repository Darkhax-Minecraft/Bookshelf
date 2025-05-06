package net.darkhax.bookshelf.common.mixin.patch.gui;

import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MenuType.class)
public class MixinMenuType {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerMenus(new MenuRegister(provider.contentNamespace())));
    }
}
package net.darkhax.bookshelf.fabric.mixin.patch;

import net.darkhax.bookshelf.common.api.registry.register.RegisterItemTab;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(CreativeModeTabs.class)
public class MixinCreativeModeTabs {

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void bootstrap(Registry<CreativeModeTab> registry, CallbackInfoReturnable<CreativeModeTab> cir) {
        final BiConsumer<ResourceLocation, CreativeModeTab> registerFunc = (rl, tab) -> Registry.register(registry, rl, tab);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            provider.registerItemTabs(new RegisterItemTab(provider.contentNamespace(), registerFunc));
        });
    }
}
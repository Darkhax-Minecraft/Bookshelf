package net.darkhax.bookshelf.common.mixin.patch.potions;

import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.class)
public class MixinPotionBrewing {

    @Inject(method = "addVanillaMixes", at = @At("RETURN"))
    private static void onBootstrap(PotionBrewing.Builder builder, CallbackInfo ci) {
        Services.CONTENT.get().forEach(provider -> provider.defineBrews(builder));
        System.out.println("build potions");
    }
}
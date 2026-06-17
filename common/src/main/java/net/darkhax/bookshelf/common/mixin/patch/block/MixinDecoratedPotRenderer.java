package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(DecoratedPotRenderer.class)
public class MixinDecoratedPotRenderer {

    @Inject(method = "getSideSprite", at = @At("HEAD"), cancellable = true)
    private static void getResourceKey(Optional<Item> item, CallbackInfoReturnable<SpriteId> cir) {
        item.ifPresent(i -> {
            if (RegistrationContext.POT_SPRITES.containsKey(i)) {
                cir.setReturnValue(RegistrationContext.POT_SPRITES.get(i));
            }
        });
    }
}
package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public class MixinDecoratedPotPatterns {

    @Inject(method = "getPatternFromItem", at = @At("TAIL"), cancellable = true)
    private static void getResourceKey(Item item, CallbackInfoReturnable<ResourceKey<DecoratedPotPattern>> cbi) {
        if (RegistrationContext.POT_PATTERN_ITEMS.containsKey(item)) {
            cbi.setReturnValue(RegistrationContext.POT_PATTERN_ITEMS.get(item));
        }
    }
}
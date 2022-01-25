package net.darkhax.bookshelf.mixin.loot;

import net.darkhax.bookshelf.api.damagesource.FakePlayerDamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemKilledByPlayerCondition.class)
public class MixinLootItemKilledByPlayerCondition {

    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("HEAD"), cancellable = true)
    public void test(LootContext context, CallbackInfoReturnable<Boolean> callback) {

        if (context != null && context.hasParam(LootContextParams.DAMAGE_SOURCE) && context.getParam(LootContextParams.DAMAGE_SOURCE) instanceof FakePlayerDamageSource fakePlayerSource) {

            if (fakePlayerSource.shouldDropPlayerLoot(context)) {

                callback.setReturnValue(true);
            }
        }
    }
}

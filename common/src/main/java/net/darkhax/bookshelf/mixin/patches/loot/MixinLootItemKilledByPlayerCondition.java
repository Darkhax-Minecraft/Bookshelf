package net.darkhax.bookshelf.mixin.patches.loot;

import net.darkhax.bookshelf.api.data.BookshelfTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemKilledByPlayerCondition.class)
public class MixinLootItemKilledByPlayerCondition {

    /**
     * If the loot context contains a damage source in the bookshelf:cause_player_only_drops tag player kill loot will
     * be allowed to generate when it otherwise wouldn't.
     */
    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("HEAD"), cancellable = true)
    public void test(LootContext context, CallbackInfoReturnable<Boolean> callback) {

        if (context != null && context.hasParam(LootContextParams.DAMAGE_SOURCE)) {

            final DamageSource source = context.getParam(LootContextParams.DAMAGE_SOURCE);

            if (source != null && source.is(BookshelfTags.DamageTypes.CAUSE_PLAYER_ONLY_DROPS)) {
                callback.setReturnValue(true);
            }
        }
    }
}

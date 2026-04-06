package net.darkhax.bookshelf.common.mixin.patch.loot;

import net.darkhax.bookshelf.common.api.data.BookshelfTags;
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
     * This patch allows mobs that were killed with Bookshelfs fake player damage to satisfy the
     * minecraft:killed_by_player loot condition.
     */
    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("HEAD"), cancellable = true)
    public void test(LootContext context, CallbackInfoReturnable<Boolean> callback) {
        if (context != null && context.hasParameter(LootContextParams.DAMAGE_SOURCE)) {
            final DamageSource source = context.getParameter(LootContextParams.DAMAGE_SOURCE);
            if (source.is(BookshelfTags.FAKE_PLAYER_DAMAGE)) {
                callback.setReturnValue(true);
            }
        }
    }
}
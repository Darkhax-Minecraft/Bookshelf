package net.darkhax.bookshelf.forge.mixin.patch.loot;

import com.mojang.serialization.Codec;
import net.darkhax.bookshelf.common.impl.data.loot.modifiers.FingerprintCodec;
import net.minecraft.world.level.storage.loot.LootPool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootPool.class)
public class MixinLootPool {

    @Shadow
    @Final
    @Mutable
    public static Codec<LootPool> CONDITIONAL_CODEC;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        CONDITIONAL_CODEC = new FingerprintCodec<>(CONDITIONAL_CODEC);
    }
}
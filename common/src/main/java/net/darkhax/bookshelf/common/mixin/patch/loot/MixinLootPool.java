package net.darkhax.bookshelf.common.mixin.patch.loot;

import com.mojang.serialization.Codec;
import net.darkhax.bookshelf.common.impl.data.loot.modifiers.FingerprintCodec;
import net.darkhax.bookshelf.common.impl.data.loot.modifiers.ILootPoolHooks;
import net.minecraft.world.level.storage.loot.LootPool;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootPool.class)
public class MixinLootPool implements ILootPoolHooks {

    @Shadow
    @Final
    @Mutable
    public static Codec<LootPool> CODEC;

    @Unique
    private Integer bookshelf$fingerprint = null;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        CODEC = new FingerprintCodec<>(CODEC);
    }

    @Override
    public void bookshelf$setHash(int fingerprint) {
        this.bookshelf$fingerprint = fingerprint;
    }

    @Override
    public Integer bookshelf$getHash() {
        return this.bookshelf$fingerprint;
    }
}

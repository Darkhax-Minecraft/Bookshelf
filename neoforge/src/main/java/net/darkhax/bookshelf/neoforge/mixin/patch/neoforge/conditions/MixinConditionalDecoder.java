package net.darkhax.bookshelf.neoforge.mixin.patch.neoforge.conditions;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "net.neoforged.neoforge.common.conditions.ConditionalOps$ConditionalDecoder")
public class MixinConditionalDecoder {

    @Inject(method = "decode", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void decode(DynamicOps ops, Object input, CallbackInfoReturnable<DataResult<Pair<Optional<WithConditions>, Object>>> cir) {
        if (input instanceof JsonObject jsonObj && !LoadConditions.canLoad(jsonObj)) {
            cir.setReturnValue(DataResult.success(Pair.of(Optional.empty(), input)));
        }
    }
}
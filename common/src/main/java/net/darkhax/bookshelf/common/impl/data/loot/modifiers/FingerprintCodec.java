package net.darkhax.bookshelf.common.impl.data.loot.modifiers;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * A codec wrapper that adds functionality to compute and set a fingerprint hash for certain objects during decoding.
 * This is achieved by hashing the JSON representation of the input.
 * <p>
 * This implementation is only intended for the LootPool codec. Loot pools do not have their own identity, so we use
 * this hash to keep track of their position within a LootTable and to detect if a user has overwritten the input.
 *
 * @param <T> The type for the codec to serialize.
 */
public class FingerprintCodec<T> implements Codec<T> {

    private final Codec<T> delegate;

    public FingerprintCodec(Codec<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        final DataResult<Pair<T, T1>> result = this.delegate.decode(ops, input);
        if (input instanceof JsonElement json && result.error().isEmpty()) {
            result.result().ifPresent(r -> {
                if (r.getFirst() instanceof ILootPoolHooks hooks) {
                    hooks.bookshelf$setHash(json.toString().hashCode());
                }
            });
        }
        return result;
    }

    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
        return this.delegate.encode(input, ops, prefix);
    }
}
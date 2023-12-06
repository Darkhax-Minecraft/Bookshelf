package net.darkhax.bookshelf.api.data.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateCodec implements Codec<BlockState> {

    @Override
    public <T> DataResult<Pair<BlockState, T>> decode(DynamicOps<T> ops, T input) {
        return null;
    }

    @Override
    public <T> DataResult<T> encode(BlockState input, DynamicOps<T> ops, T prefix) {
        return null;
    }
}
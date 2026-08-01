package net.darkhax.bookshelf.common.api.block;

import net.minecraft.client.color.block.BlockTintSource;

import java.util.function.Consumer;

public interface ITintedBlock {

    void getBlockTint(Consumer<BlockTintSource> sources);
}
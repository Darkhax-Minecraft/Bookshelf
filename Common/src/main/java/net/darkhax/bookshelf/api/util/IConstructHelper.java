package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface IConstructHelper {

    ICreativeTabBuilder creativeTab(String namespace, String tabName);

    default <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Supplier<Collection<Block>> blocks) {

        return this.blockEntityType(factory, blocks.get().toArray(new Block[0]));
    }

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks);
}
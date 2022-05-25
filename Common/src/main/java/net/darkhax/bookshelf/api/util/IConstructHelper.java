package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface IConstructHelper {

    ICreativeTabBuilder creativeTab(String namespace, String tabName);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Supplier<Block>... blocks);
}
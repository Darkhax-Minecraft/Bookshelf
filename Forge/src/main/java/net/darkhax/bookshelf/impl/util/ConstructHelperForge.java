package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.darkhax.bookshelf.impl.item.CreativeTabBuilderForge;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructHelperForge implements IConstructHelper {

    @Override
    public ICreativeTabBuilder creativeTab(String namespace, String tabName) {

        return new CreativeTabBuilderForge(new ResourceLocation(namespace, tabName));
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Supplier<Block>... validBlocks) {

        return () -> BlockEntityType.Builder.of(factory::apply, Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new)).build(null);
    }
}

package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructBuilderFabric implements IConstructHelper {

    @Override
    public ICreativeTabBuilder creativeTab(String namespace, String tabName) {

        return new net.darkhax.bookshelf.impl.item.CreativeTabBuilderFabric(new ResourceLocation(namespace, tabName));
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Supplier<Block>... validBlocks) {

        return () -> FabricBlockEntityTypeBuilder.create(factory::apply, Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new)).build();
    }
}

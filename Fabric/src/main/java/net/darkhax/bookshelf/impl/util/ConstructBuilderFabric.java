package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructBuilderFabric implements IConstructHelper {

    @Override
    public ICreativeTabBuilder creativeTab(String namespace, String tabName) {

        return new net.darkhax.bookshelf.impl.item.CreativeTabBuilderFabric(new ResourceLocation(namespace, tabName));
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {

        return () -> FabricBlockEntityTypeBuilder.create(factory::apply, validBlocks).build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {

        return new ExtendedScreenHandlerType<>(constructor::apply);
    }
}

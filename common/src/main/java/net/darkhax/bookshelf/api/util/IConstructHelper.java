package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.api.item.tab.IDisplayGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface IConstructHelper {

    default <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Supplier<Collection<Block>> blocks) {

        return this.blockEntityType(factory, blocks.get().toArray(new Block[0]));
    }

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks);

    <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor);

    CreativeModeTab.DisplayItemsGenerator wrapDisplayGen(IDisplayGenerator displayGen);
}
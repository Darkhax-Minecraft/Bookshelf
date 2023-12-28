package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.tab.IDisplayGenerator;
import net.darkhax.bookshelf.api.item.tab.IOutputWrapper;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructBuilderFabric implements IConstructHelper {

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(Class<T> blockEntityClass, BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {

        return () -> {
            final BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::apply, validBlocks).build();
            IConstructHelper.TYPE_CLASSES.putIfAbsent(type, blockEntityClass);
            return type;
        };
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {

        return new ExtendedScreenHandlerType<>(constructor::apply);
    }

    @Override
    public CreativeModeTab.DisplayItemsGenerator wrapDisplayGen(IDisplayGenerator displayGen) {

        return (flags, output) -> displayGen.display(flags, new IOutputWrapper() {
            @Override
            public void accept(ItemStack stack) {
                output.accept(stack);
            }

            @Override
            public void accept(ItemLike item) {
                output.accept(item);
            }
        });
    }
}
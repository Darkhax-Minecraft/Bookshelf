package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.item.tab.IDisplayGenerator;
import net.darkhax.bookshelf.api.item.tab.IOutputWrapper;
import net.darkhax.bookshelf.api.util.IConstructHelper;
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
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructHelperForge implements IConstructHelper {

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {

        return () -> BlockEntityType.Builder.of(factory::apply, validBlocks).build(null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {

        return IForgeMenuType.create(constructor::apply);
    }

    @Override
    public CreativeModeTab.DisplayItemsGenerator wrapDisplayGen(IDisplayGenerator displayGen) {

        return (flags, output, op) -> displayGen.display(flags, new IOutputWrapper() {
            @Override
            public void accept(ItemStack stack) {
                output.accept(stack);
            }

            @Override
            public void accept(ItemLike item) {
                output.accept(item);
            }
        }, op);
    }
}

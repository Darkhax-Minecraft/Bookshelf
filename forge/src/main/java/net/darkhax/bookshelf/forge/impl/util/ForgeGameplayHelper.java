package net.darkhax.bookshelf.forge.impl.util;

import net.darkhax.bookshelf.common.api.registry.register.RegisterMenuScreen;
import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.BiFunction;

public class ForgeGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack getCraftingRemainder(ItemStack input) {
        final Item item = input.getItem();
        return item.hasCraftingRemainingItem(input) ? item.getCraftingRemainingItem(input) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        final BlockEntity be = level.getBlockEntity(pos);
        if (be != null && !be.isRemoved()) {
            final IItemHandler inventory = be.getCapability(ForgeCapabilities.ITEM_HANDLER, side).resolve().orElse(null);
            if (inventory != null) {
                return ItemHandlerHelper.insertItemStacked(inventory, stack, false);
            }
        }
        return IGameplayHelper.super.inventoryInsert(level, pos, side, stack);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType.Builder<T> blockEntityBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
        BlockEntityType.BlockEntitySupplier<T> supplier = factory::apply;
        return BlockEntityType.Builder.of(supplier, validBlocks);
    }

    @Override
    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindMenu(MenuType<? extends M> type, RegisterMenuScreen.ScreenFactory<M, U> factory) {
        final MenuScreens.ScreenConstructor<M, U> screenFactory = factory::create;
        MenuScreens.register(type, screenFactory);
    }
}
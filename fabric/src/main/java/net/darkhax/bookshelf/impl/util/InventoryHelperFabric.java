package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.util.IInventoryHelper;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InventoryHelperFabric implements IInventoryHelper {

    @Override
    public void openMenu(ServerPlayer player, MenuProvider vanillaProvider, Consumer<FriendlyByteBuf> bufConsumer, boolean allowFakes) {

        if (allowFakes || !isFakePlayer(player)) {

            final ExtendedScreenHandlerFactory extendedProvider = new ExtendedScreenHandlerFactory() {

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {

                    return vanillaProvider.createMenu(i, inventory, player);
                }

                @Override
                public Component getDisplayName() {

                    return vanillaProvider.getDisplayName();
                }

                @Override
                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {

                    bufConsumer.accept(buf);
                }
            };

            player.openMenu(extendedProvider);
        }
    }

    @Override
    public boolean isFakePlayer(Player player) {

        return player instanceof FakePlayer;
    }
}
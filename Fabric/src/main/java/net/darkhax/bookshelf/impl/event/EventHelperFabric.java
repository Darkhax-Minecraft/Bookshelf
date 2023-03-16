package net.darkhax.bookshelf.impl.event;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.block.IFarmlandTrampleListener;
import net.darkhax.bookshelf.api.event.client.IRecipeSyncEvent;
import net.darkhax.bookshelf.api.event.entity.player.IPlayerWakeUpEvent;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class EventHelperFabric implements IEventHelper {

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            ItemTooltipCallback.EVENT.register((s, f, l) -> listener.apply(s, l, f));
        }
    }

    @Override
    public void addPlayerWakeUpListener(IPlayerWakeUpEvent listener) {

        EntitySleepEvents.STOP_SLEEPING.register((entity, pos) -> {

            if (entity instanceof Player player) {

                listener.apply(player);
            }
        });
    }

    @Override
    public void addRecipeSyncListener(IRecipeSyncEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            FabricBookshelfEvents.RECIPE_SYNC.register(listener);
        }
    }

    @Override
    public void addFarmlandTrampleListener(IFarmlandTrampleListener listener) {

        FabricBookshelfEvents.FARMLAND_TRAMPLE_EVENT.register(listener);
    }

    @Override
    public void addItemAttributeListener(IItemAttributeEvent.Listener listener) {

        FabricBookshelfEvents.ITEM_ATTRIBUTE_EVENT.register(listener);
    }
}
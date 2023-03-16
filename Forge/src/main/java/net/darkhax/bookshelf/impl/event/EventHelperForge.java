package net.darkhax.bookshelf.impl.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.block.IFarmlandTrampleListener;
import net.darkhax.bookshelf.api.event.client.IRecipeSyncEvent;
import net.darkhax.bookshelf.api.event.entity.player.IPlayerWakeUpEvent;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class EventHelperForge implements IEventHelper {

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ItemTooltipEvent.class, e -> listener.apply(e.getItemStack(), e.getToolTip(), e.getFlags()));
        }
    }

    @Override
    public void addPlayerWakeUpListener(IPlayerWakeUpEvent listener) {

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerWakeUpEvent.class, e -> listener.apply(e.getEntity()));
    }

    @Override
    public void addRecipeSyncListener(IRecipeSyncEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RecipesUpdatedEvent.class, e -> listener.apply(e.getRecipeManager()));
        }
    }

    @Override
    public void addFarmlandTrampleListener(IFarmlandTrampleListener listener) {

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, BlockEvent.FarmlandTrampleEvent.class, e -> {

            if (listener.apply(e.getEntity(), e.getPos(), e.getState())) {

                e.setCanceled(true);
            }
        });
    }

    private static final Multimap<EventPriority, IItemAttributeEvent.Listener> ITEM_ATTRIBUTE_LISTENERS = HashMultimap.create();

    @Override
    public void addItemAttributeListener(IItemAttributeEvent.Listener listener) {

        EventPriority priority = EventPriority.NORMAL;

        if (!ITEM_ATTRIBUTE_LISTENERS.containsKey(priority)) {
            MinecraftForge.EVENT_BUS.addListener(priority, false, ItemAttributeModifierEvent.class, e -> {
                final IItemAttributeEvent wrapper = new ForgeItemAttributeEvent(e);
                ITEM_ATTRIBUTE_LISTENERS.get(priority).forEach(entry -> entry.accept(wrapper));
            });
        }

        ITEM_ATTRIBUTE_LISTENERS.put(priority, listener);
    }
}
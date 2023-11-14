package net.darkhax.bookshelf.impl.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.block.IFarmlandTrampleListener;
import net.darkhax.bookshelf.api.event.client.IRecipeSyncEvent;
import net.darkhax.bookshelf.api.event.entity.IItemUseTickEvent;
import net.darkhax.bookshelf.api.event.entity.player.IPlayerWakeUpEvent;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class EventHelperNeoForge implements IEventHelper {

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener, Ordering ordering) {
        if (Services.PLATFORM.isPhysicalClient()) {
            NeoForge.EVENT_BUS.addListener(priority(ordering), false, ItemTooltipEvent.class, e -> listener.apply(e.getItemStack(), e.getToolTip(), e.getFlags()));
        }
    }

    @Override
    public void addPlayerWakeUpListener(IPlayerWakeUpEvent listener, Ordering ordering) {
        NeoForge.EVENT_BUS.addListener(priority(ordering), false, PlayerWakeUpEvent.class, e -> listener.apply(e.getEntity()));
    }

    @Override
    public void addRecipeSyncListener(IRecipeSyncEvent listener, Ordering ordering) {
        if (Services.PLATFORM.isPhysicalClient()) {
            NeoForge.EVENT_BUS.addListener(priority(ordering), false, RecipesUpdatedEvent.class, e -> listener.apply(e.getRecipeManager()));
        }
    }

    @Override
    public void addFarmlandTrampleListener(IFarmlandTrampleListener listener, Ordering ordering) {

        final EventPriority priority = priority(ordering);

        NeoForge.EVENT_BUS.addListener(priority, false, BlockEvent.FarmlandTrampleEvent.class, e -> {

            if (listener.apply(e.getEntity(), e.getPos(), e.getState())) {

                e.setCanceled(true);
            }
        });
    }

    private static final Multimap<EventPriority, IItemAttributeEvent.Listener> ITEM_ATTRIBUTE_LISTENERS = HashMultimap.create();

    @Override
    public void addItemAttributeListener(IItemAttributeEvent.Listener listener, Ordering ordering) {

        final EventPriority priority = priority(ordering);

        if (!ITEM_ATTRIBUTE_LISTENERS.containsKey(priority)) {
            NeoForge.EVENT_BUS.addListener(priority, false, ItemAttributeModifierEvent.class, e -> {
                final IItemAttributeEvent wrapper = new NeoForgeItemAttributeEvent(e);
                ITEM_ATTRIBUTE_LISTENERS.get(priority).forEach(entry -> entry.accept(wrapper));
            });
        }

        ITEM_ATTRIBUTE_LISTENERS.put(priority, listener);
    }

    private static final Multimap<EventPriority, IItemUseTickEvent> ITEM_USE_TICK_LISTENERS = HashMultimap.create();

    @Override
    public void addItemUseTickListener(IItemUseTickEvent listener, Ordering ordering) {

        final EventPriority priority = priority(ordering);

        if (!ITEM_USE_TICK_LISTENERS.containsKey(priority)) {

            NeoForge.EVENT_BUS.addListener(priority(ordering), false, LivingEntityUseItemEvent.Tick.class, e -> {
                final AtomicInteger duration = new AtomicInteger(e.getDuration());
                ITEM_USE_TICK_LISTENERS.get(priority).forEach(entry -> entry.onUseTick(e.getEntity(), e.getItem(), duration));
                e.setDuration(duration.get());
            });
        }

        ITEM_USE_TICK_LISTENERS.put(priority, listener);
    }

    private static EventPriority priority(Ordering ordering) {
        return switch (ordering) {
            case BEFORE -> EventPriority.HIGH;
            case DEFAULT -> EventPriority.NORMAL;
            case AFTER -> EventPriority.LOW;
            default -> EventPriority.NORMAL;
        };
    }
}
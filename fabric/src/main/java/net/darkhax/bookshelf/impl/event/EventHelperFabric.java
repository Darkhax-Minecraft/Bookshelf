package net.darkhax.bookshelf.impl.event;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.block.IFarmlandTrampleListener;
import net.darkhax.bookshelf.api.event.client.IRecipeSyncEvent;
import net.darkhax.bookshelf.api.event.entity.IItemUseTickEvent;
import net.darkhax.bookshelf.api.event.entity.player.IPlayerWakeUpEvent;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class EventHelperFabric implements IEventHelper {

    public static final ResourceLocation PHASE_BEFORE = Constants.id("before");
    public static final ResourceLocation PHASE_AFTER = Constants.id("after");

    static {

        setup(EntitySleepEvents.STOP_SLEEPING);
        setup(FabricBookshelfEvents.RECIPE_SYNC);
        setup(FabricBookshelfEvents.FARMLAND_TRAMPLE_EVENT);
        setup(FabricBookshelfEvents.ITEM_USE_TICK_EVENT);

        if (Services.PLATFORM.isPhysicalClient()) {

            setup(ItemTooltipCallback.EVENT);
        }
    }

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener, Ordering ordering) {

        if (Services.PLATFORM.isPhysicalClient()) {

            ItemTooltipCallback.EVENT.register(getPhase(ordering), (s, f, l) -> listener.apply(s, l, f));
        }
    }

    @Override
    public void addPlayerWakeUpListener(IPlayerWakeUpEvent listener, Ordering ordering) {

        EntitySleepEvents.STOP_SLEEPING.register(getPhase(ordering), (entity, pos) -> {

            if (entity instanceof Player player) {

                listener.apply(player);
            }
        });
    }

    @Override
    public void addRecipeSyncListener(IRecipeSyncEvent listener, Ordering ordering) {

        if (Services.PLATFORM.isPhysicalClient()) {

            FabricBookshelfEvents.RECIPE_SYNC.register(getPhase(ordering), listener);
        }
    }

    @Override
    public void addFarmlandTrampleListener(IFarmlandTrampleListener listener, Ordering ordering) {

        FabricBookshelfEvents.FARMLAND_TRAMPLE_EVENT.register(getPhase(ordering), listener);
    }

    @Override
    public void addItemAttributeListener(IItemAttributeEvent.Listener listener, Ordering ordering) {

        ModifyItemAttributeModifiersCallback.EVENT.register((stack, slot, modifiers) -> {
            listener.accept(new FabricItemAttributeEvent(stack, slot, modifiers));
        });
    }

    @Override
    public void addItemUseTickListener(IItemUseTickEvent listener, Ordering ordering) {
        FabricBookshelfEvents.ITEM_USE_TICK_EVENT.register(getPhase(ordering), listener);
    }

    private static ResourceLocation getPhase(Ordering ordering) {
        return switch (ordering) {
            case BEFORE -> PHASE_BEFORE;
            case DEFAULT -> Event.DEFAULT_PHASE;
            case AFTER -> PHASE_AFTER;
            default -> Event.DEFAULT_PHASE;
        };
    }

    private static void setup(Event<?> event) {
        event.addPhaseOrdering(PHASE_BEFORE, Event.DEFAULT_PHASE);
        event.addPhaseOrdering(Event.DEFAULT_PHASE, PHASE_AFTER);
    }
}
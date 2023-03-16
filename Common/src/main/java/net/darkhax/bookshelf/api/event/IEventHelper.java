package net.darkhax.bookshelf.api.event;

import net.darkhax.bookshelf.api.event.block.IFarmlandTrampleListener;
import net.darkhax.bookshelf.api.event.client.IRecipeSyncEvent;
import net.darkhax.bookshelf.api.event.entity.player.IPlayerWakeUpEvent;
import net.darkhax.bookshelf.api.event.item.IItemAttributeEvent;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;

/**
 * The event helper provides a loader neutral interface for registering event listeners with an underlying loader
 * specific event bus.
 */
public interface IEventHelper {

    default void addItemTooltipListener(IItemTooltipEvent listener) {
        this.addItemTooltipListener(listener, Ordering.DEFAULT);
    }

    void addItemTooltipListener(IItemTooltipEvent listener, Ordering ordering);

    default void addPlayerWakeUpListener(IPlayerWakeUpEvent listener) {
        this.addPlayerWakeUpListener(listener, Ordering.DEFAULT);
    }

    void addPlayerWakeUpListener(IPlayerWakeUpEvent listener, Ordering ordering);

    default void addRecipeSyncListener(IRecipeSyncEvent listener) {
        this.addRecipeSyncListener(listener, Ordering.DEFAULT);
    }

    void addRecipeSyncListener(IRecipeSyncEvent listener, Ordering ordering);

    default void addFarmlandTrampleListener(IFarmlandTrampleListener listener) {
        this.addFarmlandTrampleListener(listener, Ordering.DEFAULT);
    }

    void addFarmlandTrampleListener(IFarmlandTrampleListener listener, Ordering ordering);

    default void addItemAttributeListener(IItemAttributeEvent.Listener listener) {
        this.addItemAttributeListener(listener, Ordering.DEFAULT);
    }

    void addItemAttributeListener(IItemAttributeEvent.Listener listener, Ordering ordering);

    public enum Ordering {
        BEFORE, // Runs before most other mods
        DEFAULT, // Runs same time as most mods
        AFTER // Runs after most mods
    }
}
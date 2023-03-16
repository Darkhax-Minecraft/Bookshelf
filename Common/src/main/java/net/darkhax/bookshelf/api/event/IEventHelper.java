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

    /**
     * Registers a new item tooltip event listener with the underlying event bus.
     *
     * @param listener The event listener.
     */
    void addItemTooltipListener(IItemTooltipEvent listener);

    /**
     * Registers a new player wake up listener with the underlying event bus.
     *
     * @param listener The event listener.
     */
    void addPlayerWakeUpListener(IPlayerWakeUpEvent listener);

    /**
     * Registers a listener that will be invoked when the client finishes processing a recipe sync packet.
     *
     * @param listener The event listener.
     */
    void addRecipeSyncListener(IRecipeSyncEvent listener);

    /**
     * Registers a listener that will be invoked when a farmland block is trampled by an entity.
     *
     * @param listener The event listener.
     */
    void addFarmlandTrampleListener(IFarmlandTrampleListener listener);

    void addItemAttributeListener(IItemAttributeEvent.Listener listener);
}

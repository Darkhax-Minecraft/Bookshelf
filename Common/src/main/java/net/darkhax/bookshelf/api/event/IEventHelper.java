package net.darkhax.bookshelf.api.event;

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
}

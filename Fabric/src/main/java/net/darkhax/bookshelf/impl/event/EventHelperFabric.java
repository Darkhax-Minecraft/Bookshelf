package net.darkhax.bookshelf.impl.event;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class EventHelperFabric implements IEventHelper {

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            ItemTooltipCallback.EVENT.register((s, f, l) -> listener.apply(s, l, f));
        }
    }
}

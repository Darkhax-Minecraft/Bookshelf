package net.darkhax.bookshelf.impl.event;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.event.item.IItemTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class EventHelperForge implements IEventHelper {

    @Override
    public void addItemTooltipListener(IItemTooltipEvent listener) {

        if (Services.PLATFORM.isPhysicalClient()) {

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ItemTooltipEvent.class, e -> listener.apply(e.getItemStack(), e.getToolTip(), e.getFlags()));
        }
    }
}
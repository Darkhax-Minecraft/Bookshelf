package net.darkhax.bookshelf.api.event.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@FunctionalInterface
public interface IItemTooltipEvent {

    void apply(ItemStack stack, List<Component> tooltip, TooltipFlag flag);
}

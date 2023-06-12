package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.mixin.accessors.item.AccessorCooldownInstance;
import net.darkhax.bookshelf.mixin.accessors.item.AccessorItemCooldowns;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Map;

public class PlayerHelper {

    public static int getRemainingCooldownTicks(Player player, Item item) {

        if (player.getCooldowns() instanceof AccessorItemCooldowns cooldowns) {

            final Map<Item, ?> cooldownTimes = cooldowns.bookshelf$getCooldowns();

            if (cooldownTimes.get(item) instanceof AccessorCooldownInstance cooldown) {

                return cooldown.bookshelf$getEndTime() - cooldowns.bookshelf$getTickCount();
            }
        }

        return 0;
    }
}
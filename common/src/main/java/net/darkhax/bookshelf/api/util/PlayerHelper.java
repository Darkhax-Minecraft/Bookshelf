package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.mixin.accessors.item.AccessorCooldownInstance;
import net.darkhax.bookshelf.mixin.accessors.item.AccessorItemCooldowns;
import net.minecraft.ResourceLocationException;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

    public static void awardAdvancement(ServerPlayer player, ResourceLocation advancementId) {

        final AdvancementHolder toGrant = player.getServer().getAdvancements().get(advancementId);

        if (toGrant != null && toGrant.value() != null) {

            final AdvancementProgress progress = player.getAdvancements().getOrStartProgress(toGrant);

            if (!progress.isDone()) {

                for(String remainingCriteria : progress.getRemainingCriteria()) {

                    player.getAdvancements().award(toGrant, remainingCriteria);
                }
            }
        }

        else {

            throw new ResourceLocationException("No advancement found for ID: " + advancementId);
        }
    }
}
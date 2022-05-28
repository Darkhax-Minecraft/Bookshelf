package net.darkhax.bookshelf.api.event.entity.player;

import net.minecraft.world.entity.player.Player;

public interface IPlayerWakeUpEvent {

    void apply(Player player);
}

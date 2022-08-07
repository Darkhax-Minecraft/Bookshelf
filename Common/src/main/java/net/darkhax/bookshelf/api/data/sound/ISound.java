package net.darkhax.bookshelf.api.data.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface ISound {

    default void playSoundAt(Level level, @Nullable Player originator, BlockPos pos) {

        this.playSoundAt(level, originator, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
    }

    default void playSoundAt(Level level, @Nullable Player originator, Entity target) {

        this.playSoundAt(level, originator, target.getX(), target.getY(), target.getZ());
    }

    void playSoundAt(Level level, @Nullable Player originator, double posX, double posY, double posZ);
}
package net.darkhax.bookshelf.api.util;

import net.darkhax.bookshelf.mixin.accessors.client.multiplayer.AccessorClientAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface IClientHelper {

    void setRenderType(Block block, RenderType type);

    default Set<Advancement> getCompletedAdvancements(LocalPlayer player) {
        if (player.connection.getAdvancements() instanceof AccessorClientAdvancements access) {
            final Set<Advancement> completed = new HashSet<>();
            for (Map.Entry<Advancement, AdvancementProgress> entry : access.bookshelf$getProgress().entrySet()) {
                if (entry.getValue().isDone()){
                    completed.add(entry.getKey());
                }
            }
            return completed;
        }
        return Set.of();
    }
}
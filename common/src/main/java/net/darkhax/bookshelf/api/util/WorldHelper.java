package net.darkhax.bookshelf.api.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WorldHelper {

    public static void updateBlockEntity(BlockEntity blockEntity) {

        updateBlockEntity(blockEntity, false);
    }

    public static void updateBlockEntity(BlockEntity blockEntity, boolean limitDistance) {

        if (blockEntity.getLevel() instanceof ServerLevel level) {

            final Packet<?> syncPacket = blockEntity.getUpdatePacket();

            if (syncPacket != null) {

                broadcast(level, new ChunkPos(blockEntity.getBlockPos()), syncPacket, limitDistance);
            }
        }
    }

    public static void broadcast(ServerLevel level, ChunkPos pos, Packet<?> packet, boolean limitDistance) {

        level.getChunkSource().chunkMap.getPlayers(pos, limitDistance).forEach(player -> {

            if (player.connection != null) {

                player.connection.send(packet);
            }
        });
    }
}
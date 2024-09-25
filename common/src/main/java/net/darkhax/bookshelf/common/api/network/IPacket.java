package net.darkhax.bookshelf.common.api.network;

import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.annotation.OnlyFor;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.Nullable;

/**
 * Defines a custom payload packet. These packets must be registered using an
 * {@link net.darkhax.bookshelf.common.api.registry.IContentProvider}.
 *
 * @param <T> The type of the payload.
 */
public interface IPacket<T extends CustomPacketPayload> {

    /**
     * Gets the payload type.
     *
     * @return The payload type.
     */
    CustomPacketPayload.Type<T> type();

    /**
     * Gets a stream codec that can serialize the payload across the network.
     *
     * @return The stream coded used to serialize the payload.
     */
    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    /**
     * Defines how the packet is meant to be sent and where it should be handled.
     *
     * @return The intended destination of the packet.
     */
    Destination destination();

    /**
     * This method will be called when the custom payload is received. This method can be called on both the client and
     * server, depending on the destination type defined by {@link #destination()}.
     *
     * @param sender   The sender of the packet. This will always be null for packets handled on the client.
     * @param isServer True when the packet is being handled on the server.
     * @param payload  The payload that was received.
     */
    void handle(@Nullable ServerPlayer sender, boolean isServer, T payload);

    /**
     * Sends the packet from the server to a specific player.
     *
     * @param recipient The intended recipient of the payload.
     * @param payload   The payload to send.
     */
    default void toPlayer(ServerPlayer recipient, T payload) {
        if (!this.destination().handledByClient()) {
            Constants.LOG.error("Attempted to send invalid packet {} to client! Class: {} Destination: {} Payload: {}", this.type().id(), this.getClass(), this.destination(), payload.toString());
            throw new IllegalStateException("Attempted to send invalid packet " + this.type().id() + " to client!");
        }
        Services.NETWORK.sendToPlayer(recipient, payload);
    }

    /**
     * Sends the packet from the server to all connected players.
     *
     * @param level   A serverside level, used to access the player list.
     * @param payload The payload to send.
     */
    default void toAllPlayers(ServerLevel level, T payload) {
        toAllPlayers(level.getServer(), payload);
    }

    /**
     * Sends the packet from the server to all connected players.
     *
     * @param server  The server instance, used to access the player list.
     * @param payload The payload to send.
     */
    default void toAllPlayers(MinecraftServer server, T payload) {
        toAllPlayers(server.getPlayerList(), payload);
    }

    /**
     * Sends the packet from the server to all connected players.
     *
     * @param playerList The player list.
     * @param payload    The payload to send.
     */
    default void toAllPlayers(PlayerList playerList, T payload) {
        for (ServerPlayer player : playerList.getPlayers()) {
            toPlayer(player, payload);
        }
    }

    /**
     * Sends a packet from a client to the server.
     *
     * @param payload The payload to send.
     */
    @OnlyFor(PhysicalSide.CLIENT)
    default void toServer(T payload) {
        if (!this.destination().handledByServer()) {
            Constants.LOG.error("Attempted to send invalid packet {} to server! Class: {} Destination: {} Payload: {}", this.type().id(), this.getClass(), this.destination(), payload.toString());
            throw new IllegalStateException("Attempted to send invalid packet " + this.type().id() + " to server!");
        }
        if (Minecraft.getInstance().getConnection() == null) {
            Constants.LOG.error("Attempted to send packet {} before a connection to a server has been established!", this.type().id());
            throw new IllegalStateException("Attempted to send packet " + this.type().id() + " before being connected to a server!");
        }
        Services.NETWORK.sendToServer(payload);
    }
}
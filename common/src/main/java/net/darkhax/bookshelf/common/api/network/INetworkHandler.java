package net.darkhax.bookshelf.common.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Provides platform specific implementations of network related code. Access using
 * {@link net.darkhax.bookshelf.common.api.service.Services#NETWORK}.
 */
public interface INetworkHandler {

    /**
     * Registers a Bookshelf packet type to the packet registry.
     *
     * @param packet The packet type to register.
     * @param <T>    The type of the payload.
     */
    <T extends CustomPacketPayload> void register(IPacket<T> packet);

    /**
     * Sends a payload from the client to the server.
     *
     * @param payload The payload to send.
     * @param <T>     The type of the payload.
     */
    <T extends CustomPacketPayload> void sendToServer(T payload);

    /**
     * Sends a packet from the server to a player.
     *
     * @param recipient The recipient of the packet.
     * @param payload   The payload to send.
     * @param <T>       The type of the payload.
     */
    <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer recipient, T payload);

    /**
     * Tests if a payload type can be sent to a player.
     *
     * @param recipient The recipient of the packet.
     * @param payload   The payload to test.
     * @return If the payload can be sent to the recipient player.
     */
    default boolean canSendPacket(ServerPlayer recipient, CustomPacketPayload payload) {
        return this.canSendPacket(recipient, payload.type().id());
    }

    /**
     * Tests if a payload type can be sent to a player.
     *
     * @param recipient The recipient of the packet.
     * @param packet    The packet to test.
     * @return If the payload can be sent to the recipient player.
     */
    default boolean canSendPacket(ServerPlayer recipient, IPacket<?> packet) {
        return this.canSendPacket(recipient, packet.type().id());
    }

    /**
     * Tests if a payload type can be sent to a player.
     *
     * @param recipient The recipient of the packet.
     * @param payloadId The payload type ID.
     * @return If the payload can be sent to the recipient player.
     */
    boolean canSendPacket(ServerPlayer recipient, ResourceLocation payloadId);
}
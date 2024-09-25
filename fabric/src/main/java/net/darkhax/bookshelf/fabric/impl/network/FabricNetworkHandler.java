package net.darkhax.bookshelf.fabric.impl.network;

import net.darkhax.bookshelf.common.api.network.INetworkHandler;
import net.darkhax.bookshelf.common.api.network.IPacket;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class FabricNetworkHandler implements INetworkHandler {

    private static final Map<ResourceLocation, IPacket<?>> PACKETS = new HashMap<>();

    @Override
    public <T extends CustomPacketPayload> void register(IPacket<T> packet) {
        PayloadTypeRegistry.playC2S().register(packet.type(), packet.streamCodec());
        PayloadTypeRegistry.playS2C().register(packet.type(), packet.streamCodec());
        if (Services.PLATFORM.isPhysicalClient() && packet.destination().handledByClient()) {
            ClientPlayNetworking.registerGlobalReceiver(packet.type(), (payload, context) -> {
                context.client().execute(() -> {
                    packet.handle(null, false, payload);
                });
            });
        }
        if (packet.destination().handledByServer()) {
            ServerPlayNetworking.registerGlobalReceiver(packet.type(), (payload, context) -> {
                context.server().execute(() -> {
                    packet.handle(context.player(), true, payload);
                });
            });
        }
        PACKETS.put(packet.type().id(), packet);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T payload) {
        final ResourceLocation id = payload.type().id();
        if (!PACKETS.containsKey(id)) {
            Constants.LOG.error("Attempted to send unregistered packet {} to the server.", id);
            throw new IllegalStateException("Attempted to send unregistered packet " + id + " to the server.");
        }
        if (Minecraft.getInstance().player == null) {
            Constants.LOG.error("Attempted to send packet {} to the server before a player instance is available.", id);
            throw new IllegalStateException("Attempted to send packet " + id + " to the server before a player instance is available.");
        }
        ClientPlayNetworking.send(payload);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer recipient, T payload) {
        final ResourceLocation id = payload.type().id();
        if (!PACKETS.containsKey(id)) {
            Constants.LOG.error("Attempted to send unregistered packet {} to player {}.", id, recipient);
            throw new IllegalStateException("Attempted to send unregistered packet " + id + " to player " + recipient);
        }
        ServerPlayNetworking.send(recipient, payload);
    }

    @Override
    public boolean canSendPacket(ServerPlayer recipient, ResourceLocation payloadId) {
        return ServerPlayNetworking.canSend(recipient, payloadId);
    }
}
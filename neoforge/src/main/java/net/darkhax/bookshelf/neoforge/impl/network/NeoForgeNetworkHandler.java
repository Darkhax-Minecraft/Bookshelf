package net.darkhax.bookshelf.neoforge.impl.network;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.common.api.network.INetworkHandler;
import net.darkhax.bookshelf.common.api.network.IPacket;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NeoForgeNetworkHandler implements INetworkHandler {

    private static final Map<ResourceLocation, IPacket<?>> PACKETS = new HashMap<>();
    private static final Multimap<String, IPacket<?>> PACKETS_BY_NAMESPACE = HashMultimap.create();

    @Override
    public <T extends CustomPacketPayload> void register(IPacket<T> packet) {
        PACKETS.put(packet.type().id(), packet);
        PACKETS_BY_NAMESPACE.put(packet.type().id().getNamespace(), packet);
    }

    public void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        for (String namespace : PACKETS_BY_NAMESPACE.keySet()) {
            final PayloadRegistrar registrar = event.registrar(namespace).optional();
            for (IPacket packet : PACKETS_BY_NAMESPACE.get(namespace)) {
                final IPayloadHandler handler = (payload, ctx) -> packet.handle(ctx.player() instanceof ServerPlayer serverPlayer ? serverPlayer : null, !ctx.player().level().isClientSide, payload);
                switch (packet.destination()) {
                    case SERVER_TO_CLIENT ->
                            registrar.optional().commonToClient(packet.type(), packet.streamCodec(), handler);
                    case CLIENT_TO_SERVER ->
                            registrar.optional().commonToServer(packet.type(), packet.streamCodec(), handler);
                    case BIDIRECTIONAL ->
                            registrar.optional().commonBidirectional(packet.type(), packet.streamCodec(), handler);
                }
            }
        }
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
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection().send(new ServerboundCustomPayloadPacket(payload));
    }

    @Override
    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer recipient, T payload) {
        final ResourceLocation id = payload.type().id();
        if (!PACKETS.containsKey(id)) {
            Constants.LOG.error("Attempted to send unregistered packet {} to player {}.", id, recipient);
            throw new IllegalStateException("Attempted to send unregistered packet " + id + " to player " + recipient);
        }
        PacketDistributor.sendToPlayer(recipient, payload);
    }

    @Override
    public boolean canSendPacket(ServerPlayer recipient, ResourceLocation payloadId) {
        return recipient.connection.hasChannel(payloadId);
    }
}

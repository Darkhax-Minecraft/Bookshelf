package net.darkhax.bookshelf.forge.impl.network;

import io.netty.buffer.Unpooled;
import net.darkhax.bookshelf.common.api.network.INetworkHandler;
import net.darkhax.bookshelf.common.api.network.IPacket;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForgeNetworkHandler implements INetworkHandler {

    private static final Map<ResourceLocation, IPacket<?>> PACKETS = new HashMap<>();
    private static final Map<ResourceLocation, EventNetworkChannel> CHANNELS = new HashMap<>();

    @Override
    public <T extends CustomPacketPayload> void register(IPacket<T> packet) {
        final ResourceLocation id = packet.type().id();
        if (CHANNELS.containsKey(id)) {
            Constants.LOG.error("Packet {} has already been registered!", id);
            throw new IllegalStateException("Packet " + id + " has already been registered!");
        }
        final EventNetworkChannel channel = ChannelBuilder.named(id).optional().eventNetworkChannel().addListener(event -> {
            try {
                final CustomPayloadEvent.Context ctx = event.getSource();
                final T payload = packet.streamCodec().decode((RegistryFriendlyByteBuf) Objects.requireNonNull(event.getPayload()));
                ctx.setPacketHandled(true);
                ctx.enqueueWork(() -> {
                    if (ctx.isServerSide() && packet.destination().handledByServer() || ctx.isClientSide() && packet.destination().handledByClient()) {
                        packet.handle(ctx.getSender(), ctx.isServerSide(), payload);
                    }
                    else {
                        Constants.LOG.error("Attempted to handle packet {} in invalid environment. Destination: {} Payload: {}", id, packet.destination(), payload);
                    }
                });
            }
            catch (Exception e) {
                Constants.LOG.error("Error while handling packet {}.", id, e);
                throw e;
            }
        });
        PACKETS.put(id, packet);
        CHANNELS.put(id, channel);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T payload) {
        final ResourceLocation id = payload.type().id();
        if (!CHANNELS.containsKey(id)) {
            Constants.LOG.error("Attempted to send unregistered packet {} to the server.", id);
            throw new IllegalStateException("Attempted to send unregistered packet " + id + " to the server.");
        }
        if (Minecraft.getInstance().player == null) {
            Constants.LOG.error("Attempted to send packet {} to the server before a player instance is available.", id);
            throw new IllegalStateException("Attempted to send packet " + id + " to the server before a player instance is available.");
        }
        try {
            final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), Minecraft.getInstance().player.registryAccess());
            final IPacket<T> packet = (IPacket<T>) PACKETS.get(id);
            packet.streamCodec().encode(buf, payload);
            CHANNELS.get(id).send(buf, Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection());
        }
        catch (Exception e) {
            Constants.LOG.error("Failed to encode payload for packet {}. Payload: {}", id, payload, e);
            throw e;
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer recipient, T payload) {
        final ResourceLocation id = payload.type().id();
        if (!CHANNELS.containsKey(id)) {
            Constants.LOG.error("Attempted to send unregistered packet {} to player {}.", id, recipient);
            throw new IllegalStateException("Attempted to send unregistered packet " + id + " to player " + recipient);
        }
        try {
            final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), recipient.registryAccess());
            final IPacket<T> packet = (IPacket<T>) PACKETS.get(id);
            packet.streamCodec().encode(buf, payload);
            CHANNELS.get(id).send(buf, recipient.connection.getConnection());
        }
        catch (Exception e) {
            Constants.LOG.error("Failed to encode payload for packet {}. Payload: {}", id, payload, e);
            throw e;
        }
    }

    @Override
    public boolean canSendPacket(ServerPlayer recipient, ResourceLocation payloadId) {
        return CHANNELS.get(payloadId) != null && CHANNELS.get(payloadId).isRemotePresent(recipient.connection.getConnection());
    }
}
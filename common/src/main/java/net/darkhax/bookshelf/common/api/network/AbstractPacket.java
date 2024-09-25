package net.darkhax.bookshelf.common.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * A basic packet implementation.
 *
 * @param <T> The type of the payload.
 */
public abstract class AbstractPacket<T extends CustomPacketPayload> implements IPacket<T> {

    /**
     * The type of the payload.
     */
    private final CustomPacketPayload.Type<T> type;

    /**
     * A codec to serialize the payload.
     */
    private final StreamCodec<RegistryFriendlyByteBuf, T> codec;

    /**
     * The intended destination of the payload.
     */
    private final Destination direction;

    /**
     * A packet that is sent from the server to the client.
     *
     * @param id    The packet ID.
     * @param codec The payload codec.
     */
    public AbstractPacket(ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        this(id, codec, Destination.SERVER_TO_CLIENT);
    }

    /**
     * A simple packet type.
     *
     * @param id        The packet ID.
     * @param codec     The payload codec.
     * @param direction The intended destination of the packet.
     */
    public AbstractPacket(ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec, Destination direction) {
        this.type = new CustomPacketPayload.Type<>(id);
        this.codec = codec;
        this.direction = direction;
    }

    @Override
    public CustomPacketPayload.Type<T> type() {
        return this.type;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return this.codec;
    }

    @Override
    public Destination destination() {
        return this.direction;
    }
}
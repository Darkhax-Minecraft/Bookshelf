package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.network.IPacket;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public record PacketAdapter(RegistrationContext context, Consumer<IPacket<?>> registerFunc) {
    public <T extends CustomPacketPayload> IPacket<T> add(IPacket<T> packet) {
        registerFunc.accept(packet);
        return packet;
    }
}
package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.network.IPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public record RegisterPacket(String owner, Consumer<IPacket<?>> registerFunc) {
    public <T extends CustomPacketPayload> IPacket<T> add(IPacket<T> packet) {
        registerFunc.accept(packet);
        return packet;
    }
}
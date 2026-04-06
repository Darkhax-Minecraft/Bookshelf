package net.darkhax.bookshelf.common.api.data.codecs;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class EnumStreamCodec<T extends Enum<T>> implements StreamCodec<FriendlyByteBuf, T> {

    private final Class<T> enumClass;

    public EnumStreamCodec(Class<T> clazz) {
        this.enumClass = clazz;
    }

    @NotNull
    @Override
    public T decode(FriendlyByteBuf buf) {
        return buf.readEnum(enumClass);
    }

    @Override
    public void encode(FriendlyByteBuf buf, @NotNull T toWrite) {
        buf.writeEnum(toWrite);
    }
}
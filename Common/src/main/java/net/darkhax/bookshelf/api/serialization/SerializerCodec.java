package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class SerializerCodec<T> implements ISerializer<T> {

    private final Codec<T> codec;

    public SerializerCodec(Codec<T> codec) {

        this.codec = codec;
    }

    @Override
    public T fromJSON(JsonElement json) {
        return this.codec.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }

    @Override
    public JsonElement toJSON(T toWrite) {
        return this.codec.encodeStart(JsonOps.INSTANCE, toWrite).result().orElseThrow();
    }

    @Override
    public T fromByteBuf(FriendlyByteBuf buffer) {

        final CompoundTag tag = Serializers.COMPOUND_TAG.fromByteBuf(buffer);
        return this.fromNBT(tag.get("data"));
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, T toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.put("data", this.toNBT(toWrite));
        Serializers.COMPOUND_TAG.toByteBuf(buffer, tag);
    }

    @Override
    public Tag toNBT(T toWrite) {
        return this.codec.encodeStart(NbtOps.INSTANCE, toWrite).result().orElseThrow();
    }

    @Override
    public T fromNBT(Tag nbt) {
        return this.codec.decode(NbtOps.INSTANCE, nbt).result().orElseThrow().getFirst();
    }
}

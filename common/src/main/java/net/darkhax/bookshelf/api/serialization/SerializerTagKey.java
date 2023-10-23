package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Function;

public class SerializerTagKey<T> implements ISerializer<TagKey<T>> {

    private final ResourceKey<? extends Registry<T>> registryKey;

    public SerializerTagKey(ResourceKey<? extends Registry<T>> registryKey) {

        this.registryKey = registryKey;
    }

    @Override
    public TagKey<T> fromJSON(JsonElement json) {

        return TagKey.create(this.registryKey, Serializers.RESOURCE_LOCATION.fromJSON(json));
    }

    @Override
    public JsonElement toJSON(TagKey<T> toWrite) {

        return Serializers.RESOURCE_LOCATION.toJSON(toWrite.location());
    }

    @Override
    public TagKey<T> fromByteBuf(FriendlyByteBuf buffer) {

        return TagKey.create(this.registryKey, Serializers.RESOURCE_LOCATION.fromByteBuf(buffer));
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, TagKey<T> toWrite) {

        Serializers.RESOURCE_LOCATION.toByteBuf(buffer, toWrite.location());
    }

    @Override
    public Tag toNBT(TagKey<T> toWrite) {

        return Serializers.RESOURCE_LOCATION.toNBT(toWrite.location());
    }

    @Override
    public TagKey<T> fromNBT(Tag nbt) {

        return TagKey.create(this.registryKey, Serializers.RESOURCE_LOCATION.fromNBT(nbt));
    }
}
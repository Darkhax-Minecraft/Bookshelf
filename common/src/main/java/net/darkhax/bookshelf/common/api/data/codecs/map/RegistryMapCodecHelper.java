package net.darkhax.bookshelf.common.api.data.codecs.map;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class RegistryMapCodecHelper<T> extends MapCodecHelper<Holder<T>> {

    private final MapCodecHelper<TagKey<T>> tagHelper;

    private RegistryMapCodecHelper(Codec<Holder<T>> holderCodec, ResourceKey<Registry<T>> key) {
        super(holderCodec);
        this.tagHelper = new MapCodecHelper<>(TagKey.codec(key));
    }

    public MapCodecHelper<TagKey<T>> tag() {
        return this.tagHelper;
    }

    /**
     * Creates a Codec helper for a builtin registry.
     *
     * @param registry The registry to create a codec helper for.
     * @param <T>      The type of value held by the registry.
     * @return A Codec helper for a builtin registry.
     */
    public static <T> RegistryMapCodecHelper<T> create(Registry<T> registry) {
        return new RegistryMapCodecHelper<>(registry.holderByNameCodec(), (ResourceKey<Registry<T>>) registry.key());
    }

    /**
     * Creates a Codec helper for a datapack registry. This codec can only be used when registry access is available
     * through RegistryOps.
     *
     * @param key The key of the registry to use.
     * @param <T> The type of value held by the registry.
     * @return A Codec helper for datapack entries.
     */
    public static <T> RegistryMapCodecHelper<T> create(ResourceKey<Registry<T>> key) {
        return new RegistryMapCodecHelper<>(RegistryFixedCodec.create(key), key);
    }
}
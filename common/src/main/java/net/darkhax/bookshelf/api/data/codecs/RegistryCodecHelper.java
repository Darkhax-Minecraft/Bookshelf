package net.darkhax.bookshelf.api.data.codecs;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

public class RegistryCodecHelper<T> extends CodecHelper<T> {

    private final Registry<T> registry;
    private final CodecHelper<TagKey<T>> tagHelper;

    public RegistryCodecHelper(Registry<T> registry) {

        super(BookshelfCodecs.registry(registry));
        this.registry = registry;
        this.tagHelper = new CodecHelper<>(TagKey.codec(registry.key()));
    }

    public CodecHelper<TagKey<T>> tag() {

        return this.tagHelper;
    }

    public Registry<T> getRegistry() {

        return this.registry;
    }
}
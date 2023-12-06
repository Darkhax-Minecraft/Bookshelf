package net.darkhax.bookshelf.api.data.bytebuf;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class RegistryByteBufHelper<T> extends ByteBufHelper<T> {

    private final Registry<T> registry;
    private final ByteBufHelper<TagKey<T>> tagHelper;

    public RegistryByteBufHelper(Registry<T> registry) {

        super(
                buffer -> {
                    final ResourceLocation id = buffer.readResourceLocation();
                    return registry.containsKey(id) ? registry.get(id) : null;
                },
                (buffer, entry) -> buffer.writeResourceLocation(registry.getKey(entry))
        );

        this.registry = registry;
        this.tagHelper = new ByteBufHelper<>(buffer -> TagKey.create(registry.key(), buffer.readResourceLocation()), (buffer, tag) -> buffer.writeResourceLocation(tag.location()));
    }

    public ByteBufHelper<TagKey<T>> tag() {

        return this.tagHelper;
    }
}
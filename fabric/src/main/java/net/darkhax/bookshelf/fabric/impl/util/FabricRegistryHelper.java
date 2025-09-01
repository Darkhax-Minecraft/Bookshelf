package net.darkhax.bookshelf.fabric.impl.util;

import net.darkhax.bookshelf.common.api.registry2.ContentProvider;
import net.darkhax.bookshelf.common.api.registry2.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry2.adapters.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry2.adapters.GameRegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class FabricRegistryHelper {

    private final ContentProvider content;
    private final RegistrationContext context;

    public FabricRegistryHelper(ContentProvider content) {
        this.content = content;
        this.context = new RegistrationContext(content.namespace());
        this.registerContent();
    }

    private void registerContent() {
        this.content.defineBlocks(new BlockRegistryAdapter(this.context, adapt(BuiltInRegistries.BLOCK)));
        this.context.getPlaceableBlocks().forEach((ref, factory) -> Registry.register(BuiltInRegistries.ITEM, ref.key().location(), factory.apply(ref.value().get())));
        this.content.defineItems(new GameRegistryAdapter<>(this.context, Registries.ITEM, adapt(BuiltInRegistries.ITEM)));
    }

    private static <T> BiConsumer<ResourceKey<T>, Supplier<T>> adapt(Registry<T> registry) {
        return (key, value) -> Registry.register(registry, key, value.get());
    }
}

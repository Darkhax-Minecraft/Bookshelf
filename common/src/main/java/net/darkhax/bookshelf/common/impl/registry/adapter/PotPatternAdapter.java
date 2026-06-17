package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A registry adapter for decorated pot patterns like sherds.
 */
public final class PotPatternAdapter extends GameRegistryAdapter<DecoratedPotPattern> {

    public PotPatternAdapter(RegistrationContext context, ResourceKey<Registry<DecoratedPotPattern>> regKey, BiConsumer<ResourceKey<DecoratedPotPattern>, Supplier<DecoratedPotPattern>> registryFunc) {
        super(context, regKey, registryFunc);
    }

    /**
     * Adds a new decorated pot pattern to the game registry.
     *
     * @param key The ID to register the value under. This ID only needs to be unique within your namespace.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceKey<DecoratedPotPattern>, DecoratedPotPattern> add(String key) {
        return this.add(key, () -> new DecoratedPotPattern(this.id(key)));
    }

    /**
     * Adds a new decorated pot pattern to the game registry and associates it with an item.
     *
     * @param key  The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param item The item to associate the pattern with.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceKey<DecoratedPotPattern>, DecoratedPotPattern> addWithItem(String key, Item item) {
        final RegistryReference<ResourceKey<DecoratedPotPattern>, DecoratedPotPattern> pattern = this.add(key);
        if (Services.PLATFORM.isPhysicalClient()) {
            this.context.addPotPatternItem(item, pattern.key());
        }
        return pattern;
    }

    /**
     * Adds a new decorated pot pattern to the game registry and associates it with an item.
     *
     * @param key  The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param item The item to associate the pattern with.
     * @return A reference to the registry entry.
     */
    public RegistryReference<ResourceKey<DecoratedPotPattern>, DecoratedPotPattern> addWithItem(String key, Supplier<Item> item) {
        return this.addWithItem(key, item.get());
    }
}
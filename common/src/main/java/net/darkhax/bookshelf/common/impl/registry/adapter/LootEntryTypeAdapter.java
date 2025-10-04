package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class LootEntryTypeAdapter extends GameRegistryAdapter<LootPoolEntryType> {
    public LootEntryTypeAdapter(RegistrationContext context, ResourceKey<Registry<LootPoolEntryType>> registry, BiConsumer<ResourceKey<LootPoolEntryType>, Supplier<LootPoolEntryType>> registryFunc) {
        super(context, registry, registryFunc);
    }

    public <T extends LootPoolEntryContainer> void add(String key, MapCodec<T> codec) {
        this.add(key, new LootPoolEntryType(codec));
    }
}
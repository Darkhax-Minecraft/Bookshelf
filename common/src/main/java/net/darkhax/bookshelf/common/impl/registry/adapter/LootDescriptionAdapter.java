package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriber;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public record LootDescriptionAdapter(BiConsumer<Identifier, LootPoolEntryDescriber> registryFunc) {

    public void add(String name, LootPoolEntryDescriber describer) {
        this.add(Identifier.tryParse(name), describer);
    }

    public void add(Identifier id, LootPoolEntryDescriber describer) {
        this.registryFunc.accept(id, describer);
    }
}
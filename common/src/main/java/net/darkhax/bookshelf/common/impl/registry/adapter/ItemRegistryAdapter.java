package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemRegistryAdapter extends GameRegistryAdapter<Item> {

    public ItemRegistryAdapter(RegistrationContext context, BiConsumer<ResourceKey<Item>, Supplier<Item>> registryFunc) {
        super(context, Registries.ITEM, registryFunc);
    }

    public RegistryReference<ResourceKey<Item>, Item> add(String key, UnaryOperator<Item.Properties> propertiesFunc) {
        return this.add(key, Item::new, propertiesFunc);
    }

    public RegistryReference<ResourceKey<Item>, Item> add(String key, Function<Item.Properties, Item> itemFunc) {
        return this.add(key, itemFunc, UnaryOperator.identity());
    }

    public RegistryReference<ResourceKey<Item>, Item> add(String key, Function<Item.Properties, Item> itemFunc, UnaryOperator<Item.Properties> propertiesFunc) {
        return this.add(key, () -> {
            final Identifier itemId = this.id(key);
            final Item.Properties properties = propertiesFunc.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, itemId)));
            return itemFunc.apply(properties);
        });
    }
}
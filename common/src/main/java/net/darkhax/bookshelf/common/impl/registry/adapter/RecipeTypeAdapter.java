package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.RegistryReference;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.impl.recipe.RecipeTypeImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RecipeTypeAdapter extends GameRegistryAdapter<RecipeType<?>> {

    public RecipeTypeAdapter(RegistrationContext context, ResourceKey<Registry<RecipeType<?>>> regKey, BiConsumer<ResourceKey<RecipeType<?>>, Supplier<RecipeType<?>>> registryFunc) {
        super(context, regKey, registryFunc);
    }

    public RegistryReference<ResourceKey<RecipeType<?>>, RecipeType<?>> add(String key) {
        return this.add(key, () -> new RecipeTypeImpl<>(ResourceLocation.fromNamespaceAndPath(context.namespace(), key)));
    }
}
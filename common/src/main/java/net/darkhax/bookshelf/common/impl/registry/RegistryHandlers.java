package net.darkhax.bookshelf.common.impl.registry;

import net.darkhax.bookshelf.common.api.registry.RegistryHandler;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterItem;
import net.darkhax.bookshelf.common.api.registry.register.RegisterRecipeType;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.recipe.RecipeTypeImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryHandlers {

    private static final Map<ResourceKey<? extends Registry<?>>, RegistryHandler> HANDLERS = buildHandlers();

    private static Map<ResourceKey<? extends Registry<?>>, RegistryHandler> buildHandlers() {
        final Map<ResourceKey<? extends Registry<?>>, RegistryHandler> handlers = new HashMap<>();
        handlers.put(Registries.MOB_EFFECT, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerMobEffects(new Register<>(provider.contentNamespace(), (id, effect) -> Registry.registerForHolder(registry, id, effect)))));
        handlers.put(Registries.BLOCK, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerMobEffects(new Register<>(provider.contentNamespace(), (id, effect) -> Registry.registerForHolder(registry, id, effect)))));
        handlers.put(Registries.ENTITY_TYPE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerEntities(new Register<>(provider.contentNamespace(), (id, builder) -> Registry.register(registry, id, builder.build(id.toString()))))));
        handlers.put(Registries.ITEM, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            provider.registerItems(new Register<>(provider.contentNamespace(), Items::registerItem));
            provider.registerItems(new RegisterItem(provider.contentNamespace(), Items::registerItem));
        }));
        handlers.put(Registries.BLOCK_ENTITY_TYPE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerBlockEntities(new Register<>(provider.contentNamespace(), (id, builder) -> Registry.register(registry, id, builder.build(null))))));
        handlers.put(Registries.MENU, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerMenus(new MenuRegister(provider.contentNamespace()))));
        handlers.put(Registries.RECIPE_TYPE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerRecipeTypes(new RegisterRecipeType(provider.contentNamespace(), id -> Registry.register(registry, id, new RecipeTypeImpl<>(id))))));
        handlers.put(Registries.RECIPE_SERIALIZER, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerRecipeSerializers(new Register<>(provider.contentNamespace(), (id, serializer) -> Registry.register(registry, id, serializer)))));
        handlers.put(Registries.ATTRIBUTE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerAttributes(new Register<>(provider.contentNamespace(), (id, attribute) -> Registry.register(registry, id, attribute)))));
        handlers.put(Registries.LOOT_CONDITION_TYPE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerLootConditions(new Register<>(provider.contentNamespace(), (id, codec) -> Registry.register(registry, id, new LootItemConditionType(codec))))));
        handlers.put(Registries.LOOT_FUNCTION_TYPE, registry -> Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerLootFunctions(new Register<>(provider.contentNamespace(), (id, codec) -> Registry.register(registry, id, new LootItemFunctionType<>(codec))))));
        return handlers;
    }

    public static <T, R extends WritableRegistry<T>> void loadContent(ResourceKey<? extends Registry<T>> key, R registry) {
        final RegistryHandler handler = HANDLERS.get(key);
        if (handler != null) {
            handler.handle(registry);
        }
    }
}

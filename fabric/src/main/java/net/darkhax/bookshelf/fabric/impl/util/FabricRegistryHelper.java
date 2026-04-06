package net.darkhax.bookshelf.fabric.impl.util;

import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.registry.adapter.*;
import net.darkhax.bookshelf.fabric.impl.data.FabricIngredient;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class FabricRegistryHelper {

    private final ContentProvider content;
    private final RegistrationContext context;

    public FabricRegistryHelper(ContentProvider content) {
        this.content = content;
        this.context = new RegistrationContext(content.namespace());
        if (content.canLoad()) {
            this.registerContent();
            this.registerCommands();
            if (Services.PLATFORM.isPhysicalClient()) {
                this.registerClient();
            }
        }
        else {
            BookshelfMod.LOG.debug("Content provider {} is disabled.", content);
        }
    }

    private void registerContent() {
        this.content.defineLoadConditions(new GenericRegistryAdapter<>(this.context, (id, val) -> LoadConditions.register(id, val.get())));
        this.content.defineBlocks(new BlockRegistryAdapter(this.context, Registries.BLOCK, adapt(BuiltInRegistries.BLOCK)));
        this.context.getPlaceableBlocks().forEach((ref, factory) -> Registry.register(BuiltInRegistries.ITEM, ref.key().identifier(), factory.apply(ref.value().get())));
        this.content.defineItems(new GameRegistryAdapter<>(this.context, Registries.ITEM, adapt(BuiltInRegistries.ITEM)));
        this.content.defineCreativeTabs(new CreativeModeTabAdapter(this.context, Registries.CREATIVE_MODE_TAB, adapt(BuiltInRegistries.CREATIVE_MODE_TAB)));
        this.content.defineIngredientTypes(new IngredientTypeAdapter(this.context, (id, value) -> CustomIngredientSerializer.register(adaptType(id, value.get()))));
        this.content.defineRecipeTypes(new RecipeTypeAdapter(this.context, Registries.RECIPE_TYPE, adapt(BuiltInRegistries.RECIPE_TYPE)));
        this.content.defineAttributes(new GameRegistryAdapter<>(this.context, Registries.ATTRIBUTE, adapt(BuiltInRegistries.ATTRIBUTE)));
        this.content.defineMobEffects(new GameRegistryAdapter<>(this.context, Registries.MOB_EFFECT, adapt(BuiltInRegistries.MOB_EFFECT)));
        this.content.defineCriteriaTriggers(new GameRegistryAdapter<>(this.context, Registries.TRIGGER_TYPE, adapt(BuiltInRegistries.TRIGGER_TYPES)));
        this.content.defineEntities(new GameRegistryAdapter<>(this.context, Registries.ENTITY_TYPE, adapt(BuiltInRegistries.ENTITY_TYPE)));
        this.content.definePotions(new GameRegistryAdapter<>(this.context, Registries.POTION, adapt(BuiltInRegistries.POTION)));
        this.content.definePotPatterns(new PotPatternAdapter(this.context, Registries.DECORATED_POT_PATTERN, adapt(BuiltInRegistries.DECORATED_POT_PATTERN)));
        this.content.defineItemComponents(new GameRegistryAdapter<>(this.context, Registries.DATA_COMPONENT_TYPE, adapt(BuiltInRegistries.DATA_COMPONENT_TYPE)));
        this.content.defineEnchantmentComponents(new GameRegistryAdapter<>(this.context, Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, adapt(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE)));
        this.content.defineBlockEntities(new GameRegistryAdapter<>(this.context, Registries.BLOCK_ENTITY_TYPE, adapt(BuiltInRegistries.BLOCK_ENTITY_TYPE)));
        this.content.defineRecipeSerializers(new GameRegistryAdapter<>(this.context, Registries.RECIPE_SERIALIZER, adapt(BuiltInRegistries.RECIPE_SERIALIZER)));
        this.content.defineLootEntryTypes(new GameRegistryAdapter<>(this.context, Registries.LOOT_POOL_ENTRY_TYPE, adapt(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE)));
        this.content.defineMenuType(new MenuTypeAdapter(this.context, (key, factory) -> Registry.register(BuiltInRegistries.MENU, key, new MenuType<>(factory.get()::create, FeatureFlags.VANILLA_SET))));
        this.content.definePackets(new PacketAdapter(this.context, Services.NETWORK::register));
        this.content.defineSounds(new SoundEventAdapter(this.context, Registries.SOUND_EVENT, adapt(BuiltInRegistries.SOUND_EVENT)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerClient() {
        this.content.defineMenuScreens(new MenuScreenAdapter((id, factory) -> MenuScreens.register(id, (MenuScreens.ScreenConstructor) factory::create)));
        this.content.defineBlockRenderers(new BlockEntityRendererAdapter(BlockEntityRenderers::register));
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(this.content::defineCommands);
        this.content.defineCommandArguments(new CommandArgumentAdapter(this.context, (rl, info) -> registerCommandArgument(rl, info.get())));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerCommandArgument(Identifier key, CommandArgumentAdapter.TypeInfo type) {
        ArgumentTypeRegistry.registerArgumentType(key, type.argType(), type.typeIfo());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static CustomIngredientSerializer adaptType(Identifier id, IngredientTypeAdapter.IngredientType type) {
        return FabricIngredient.make(id, type.codec(), type.stream());
    }

    private static <T> BiConsumer<ResourceKey<T>, Supplier<T>> adapt(Registry<T> registry) {
        return (key, value) -> Registry.register(registry, key, value.get());
    }
}

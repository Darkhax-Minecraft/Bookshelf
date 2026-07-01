package net.darkhax.bookshelf.common.api.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.annotation.OnlyFor;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.*;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

/**
 * An interface for adding custom game content such as blocks and items during the appropriate stages of the game's
 * lifecycle.
 * <p>
 * Implementations of this interface are discovered automatically by Bookshelf using the {@link java.util.ServiceLoader}
 * mechanism. To make your provider loadable, add the fully qualified name of your implementation to the file:
 * <pre>
 *     META-INF/services/net.darkhax.bookshelf.common.api.registry.ContentProvider
 * </pre>
 */
public interface ContentProvider {

    /**
     * Registers new attributes with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineAttributes(GameRegistryAdapter<Attribute> registry) {
    }

    /**
     * Registers new blocks with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineBlocks(BlockRegistryAdapter registry) {
    }

    /**
     * Registers new block entities with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineBlockEntities(GameRegistryAdapter<BlockEntityType<?>> registry) {
    }

    /**
     * Registers new items with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineItems(ItemRegistryAdapter registry) {
    }

    /**
     * Registers new recipe types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineRecipeTypes(RecipeTypeAdapter registry) {
    }

    /**
     * Registers new creative mode tabs with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineCreativeTabs(CreativeModeTabAdapter registry) {
    }

    /**
     * Registers new command argument types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineCommandArguments(CommandArgumentAdapter registry) {
    }

    /**
     * Registers new commands with the game. This may happen multiple times per game instance depending on user
     * actions.
     *
     * @param dispatcher The command dispatcher to populate with your new commands.
     * @param context    Context used to build commands.
     * @param selection  The type of commands that should be registered.
     */
    default void defineCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
    }

    /**
     * Registers new ingredient types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineIngredientTypes(IngredientTypeAdapter registry) {
    }

    /**
     * Registers new mob effects with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineMobEffects(GameRegistryAdapter<MobEffect> registry) {
    }

    /**
     * Registers new criteria triggers with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineCriteriaTriggers(GameRegistryAdapter<CriterionTrigger<?>> registry) {
    }

    /**
     * Registers entity types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineEntities(GameRegistryAdapter<EntityType<?>> registry) {
    }

    /**
     * Registers potions with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void definePotions(GameRegistryAdapter<Potion> registry) {
    }

    /**
     * Registers new potion brewing recipes with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineBrews(PotionBrewing.Builder registry) {
    }

    /**
     * Registers new decorated pot patterns with the game, and create associations between items and patterns.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void definePotPatterns(PotPatternAdapter registry) {
    }

    /**
     * Registers new item components with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineItemComponents(GameRegistryAdapter<DataComponentType<?>> registry) {
    }

    /**
     * Registers new enchantment components with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineEnchantmentComponents(GameRegistryAdapter<DataComponentType<?>> registry) {
    }

    /**
     * Registers new recipe serializers with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineRecipeSerializers(GameRegistryAdapter<RecipeSerializer<?>> registry) {
    }

    /**
     * Registers new loot entry types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineLootEntryTypes(GameRegistryAdapter<MapCodec<? extends LootPoolEntryContainer>> registry) {
    }

    /**
     * Inject entries into existing loot pools. For example, this can be used to add new loot to the dungeon loot
     * chest.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineLootPoolAdditions(LootPoolAdditionAdapter registry) {
    }

    /**
     * Registers a new descriptor for loot entries.
     *
     * @param registry Accepts registry requests.
     */
    default void defineLootDescriptions(LootDescriptionAdapter registry) {
    }

    /**
     * Registers a new bookshelf load condition for JSON resources.
     *
     * @param registry Accepts registry requests.
     */
    default void defineLoadConditions(GenericRegistryAdapter<MapCodec<? extends ILoadCondition>> registry) {
    }

    /**
     * Registers new menu types with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineMenuType(MenuTypeAdapter registry) {
    }

    /**
     * Registers new packets with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void definePackets(PacketAdapter registry) {
    }

    /**
     * Registers new sound events with the game.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    default void defineSounds(SoundEventAdapter registry) {

    }

    /**
     * Define properties for the Bookshelf property load condition.
     *
     * @param registry Register new properties.
     */
    default void defineLoadProperties(BiConsumer<Identifier, BooleanSupplier> registry) {

    }

    /**
     * Defines new types of data driven registries, like the vanilla biome or enchantment registry.
     *
     * @param registry Registers new data registries.
     */
    default void defineDataRegistries(DataRegistryAdapter registry) {
    }

    /**
     * Associates menu types with screens.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    @OnlyFor(PhysicalSide.CLIENT)
    default void defineMenuScreens(MenuScreenAdapter registry) {
    }

    /**
     * Associates a block entity with a block entity renderer.
     *
     * @param registry Adapts registry requests to the current mod loader.
     */
    @OnlyFor(PhysicalSide.CLIENT)
    default void defineBlockRenderers(BlockEntityRendererAdapter registry) {
    }

    /**
     * Gets the namespace that all content from the provider should be registered under. This MUST be the same modid
     * that is used by your NeoForge/Fabric mod.
     *
     * @return The namespace to register content with.
     */
    String namespace();

    /**
     * Checks if content from the provider should be loaded or not. All providers will be loaded by default, however
     * custom implementations may have additional requirements.
     * <p>
     * Bookshelf will still classload your provider if this returns false, this only prevents content from being loaded.
     * It is the implementers responsibility to ensure their class can be classloaded safely.
     *
     * @return If content from this provider should be loaded.
     */
    default boolean canLoad() {
        return true;
    }
}
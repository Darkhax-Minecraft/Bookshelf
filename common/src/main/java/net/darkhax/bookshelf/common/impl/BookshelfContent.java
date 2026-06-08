package net.darkhax.bookshelf.common.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.darkhax.bookshelf.common.api.commands.args.FontArgument;
import net.darkhax.bookshelf.common.api.commands.args.TagArgument;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriptions;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.command.*;
import net.darkhax.bookshelf.common.impl.data.conditions.*;
import net.darkhax.bookshelf.common.impl.data.criterion.trigger.AdvancementTrigger;
import net.darkhax.bookshelf.common.impl.data.ingredient.*;
import net.darkhax.bookshelf.common.impl.data.loot.entries.LootItemStack;
import net.darkhax.bookshelf.common.impl.recipe.smithing.ComponentSmithingRecipe;
import net.darkhax.bookshelf.common.impl.registry.adapter.CommandArgumentAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.IngredientTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.LootDescriptionAdapter;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class BookshelfContent implements ContentProvider {

    @Override
    public void defineIngredientTypes(IngredientTypeAdapter registry) {
        registry.add("any", AnyIngredient.CODEC, AnyIngredient.STREAM);
        registry.add("false", FalseIngredient.CODEC, FalseIngredient.STREAM);
        registry.add("all", AllOfIngredient.CODEC, AllOfIngredient.STREAM);
        registry.add("either", EitherIngredient.CODEC, EitherIngredient.STREAM);
        registry.add("mod_id", ModIdIngredient.CODEC, ModIdIngredient.STREAM);
        registry.add("block_tag", BlockTagIngredient.CODEC, BlockTagIngredient.STREAM);
    }

    @Override
    public void defineCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(BookshelfMod.MOD_ID).requires(PermissionLevel.MODERATOR);
        root.then(HandCommand.build(context));
        root.then(FontCommand.build());
        root.then(RenameCommand.build(context));
        root.then(EnchantCommand.build(context));
        root.then(TranslateCommand.build(context));
        root.then(StructureCommand.build());
        if (Services.PLATFORM.isDevelopmentEnvironment() && Services.PLATFORM.isPhysicalClient() && selection == Commands.CommandSelection.INTEGRATED) {
            root.then(DebugCommands.build(context));
        }
        dispatcher.register(root);
    }

    @Override
    public void defineCommandArguments(CommandArgumentAdapter registry) {
        registry.add("font", FontArgument.class, FontArgument.SERIALIZER);
        registry.add("tag", TagArgument.class, TagArgument.SERIALIZER);
    }

    @Override
    public void defineLoadConditions(GenericRegistryAdapter<MapCodec<? extends ILoadCondition>> registry) {
        registry.add(And.TYPE_ID, And.CODEC);
        registry.add(Not.TYPE_ID, Not.CODEC);
        registry.add(Or.TYPE_ID, Or.CODEC);
        registry.add(OnPlatform.TYPE_ID, OnPlatform.CODEC);
        registry.add(ModLoaded.TYPE_ID, ModLoaded.CODEC);
        registry.add(RegistryContains.BLOCK, RegistryContains.of(RegistryContains.BLOCK, BuiltInRegistries.BLOCK));
        registry.add(RegistryContains.ITEM, RegistryContains.of(RegistryContains.ITEM, BuiltInRegistries.ITEM));
        registry.add(RegistryContains.ENTITY, RegistryContains.of(RegistryContains.ENTITY, BuiltInRegistries.ENTITY_TYPE));
        registry.add(RegistryContains.BLOCK_ENTITY, RegistryContains.of(RegistryContains.BLOCK_ENTITY, BuiltInRegistries.BLOCK_ENTITY_TYPE));
        registry.add(Property.TYPE_ID, Property.CODEC);
        Services.CONTENT.get().forEach(provider -> provider.defineLoadProperties(Property::define));
    }

    @Override
    public void defineCriteriaTriggers(GameRegistryAdapter<CriterionTrigger<?>> registry) {
        registry.add("earn_advancement", AdvancementTrigger.TRIGGER);
    }

    @Override
    public void defineLootEntryTypes(GameRegistryAdapter<MapCodec<? extends LootPoolEntryContainer>> registry) {
        registry.add("item_stack", LootItemStack.MAP_CODEC);
    }

    @Override
    public void defineLootDescriptions(LootDescriptionAdapter registry) {
        registry.add("empty", LootPoolEntryDescriptions.EMPTY);
        registry.add("item", LootPoolEntryDescriptions.ITEM);
        registry.add("loot_table", LootPoolEntryDescriptions.LOOT_TABLE);
        registry.add("dynamic", LootPoolEntryDescriptions.DYNAMIC);
        registry.add("tag", LootPoolEntryDescriptions.TAG);
        registry.add("alternatives", LootPoolEntryDescriptions.COMPOSITE);
        registry.add("sequence", LootPoolEntryDescriptions.COMPOSITE);
        registry.add("group", LootPoolEntryDescriptions.COMPOSITE);
        registry.add(BookshelfMod.id("item_stack"), LootPoolEntryDescriptions.ITEM_STACK);
    }

    @Override
    public void defineRecipeSerializers(GameRegistryAdapter<RecipeSerializer<?>> registry) {
        registry.add("smithing_components", ComponentSmithingRecipe.SERIALIZER);
    }

    @Override
    public String namespace() {
        return BookshelfMod.MOD_ID;
    }
}
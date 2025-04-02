package net.darkhax.bookshelf.common.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.commands.args.FontArgument;
import net.darkhax.bookshelf.common.api.commands.args.TagArgument;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.registry.IContentProvider;
import net.darkhax.bookshelf.common.api.registry.register.ArgumentRegister;
import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.registry.register.RegisterIngredient;
import net.darkhax.bookshelf.common.api.registry.register.RegisterLootDescription;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.command.BlockTagToItemTagCommand;
import net.darkhax.bookshelf.common.impl.command.DebugCommands;
import net.darkhax.bookshelf.common.impl.command.EnchantCommand;
import net.darkhax.bookshelf.common.impl.command.FontCommand;
import net.darkhax.bookshelf.common.impl.command.HandCommand;
import net.darkhax.bookshelf.common.impl.command.RenameCommand;
import net.darkhax.bookshelf.common.impl.command.TranslateCommand;
import net.darkhax.bookshelf.common.impl.data.conditions.And;
import net.darkhax.bookshelf.common.impl.data.conditions.ModLoaded;
import net.darkhax.bookshelf.common.impl.data.conditions.Not;
import net.darkhax.bookshelf.common.impl.data.conditions.OnPlatform;
import net.darkhax.bookshelf.common.impl.data.conditions.Or;
import net.darkhax.bookshelf.common.impl.data.conditions.RegistryContains;
import net.darkhax.bookshelf.common.impl.data.criterion.item.NamespaceItemPredicate;
import net.darkhax.bookshelf.common.impl.data.criterion.trigger.AdvancementTrigger;
import net.darkhax.bookshelf.common.impl.data.ingredient.AllOfIngredient;
import net.darkhax.bookshelf.common.impl.data.ingredient.BlockTagIngredient;
import net.darkhax.bookshelf.common.impl.data.ingredient.EitherIngredient;
import net.darkhax.bookshelf.common.impl.data.ingredient.ModIdIngredient;
import net.darkhax.bookshelf.common.impl.data.loot.entries.LootItemStack;
import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriptions;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class BookshelfContent implements IContentProvider {

    @Override
    public String contentNamespace() {
        return Constants.MOD_ID;
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        final LiteralArgumentBuilder<CommandSourceStack> root = LiteralArgumentBuilder.literal(Constants.MOD_ID);
        root.then(HandCommand.build(context));
        root.then(FontCommand.build());
        root.then(RenameCommand.build(context));
        root.then(EnchantCommand.build(context));
        root.then(TranslateCommand.build(context));
        root.then(BlockTagToItemTagCommand.build(context));
        if (Services.PLATFORM.isDevelopmentEnvironment() && Services.PLATFORM.isPhysicalClient() && selection == Commands.CommandSelection.INTEGRATED) {
            root.then(DebugCommands.build(context));
        }
        dispatcher.register(root);
    }

    @Override
    public void registerCommandArguments(ArgumentRegister register) {
        register.accept("font", FontArgument.class, FontArgument.SERIALIZER);
        register.accept("tag", TagArgument.class, TagArgument.SERIALIZER);
    }

    @Override
    public void registerIngredientTypes(RegisterIngredient registry) {
        registry.add("all", AllOfIngredient.CODEC, AllOfIngredient.STREAM);
        registry.add("either", EitherIngredient.CODEC, EitherIngredient.STREAM);
        registry.add("mod_id", ModIdIngredient.CODEC, ModIdIngredient.STREAM);
        registry.add("block_tag", BlockTagIngredient.CODEC, BlockTagIngredient.STREAM);
    }

    @Override
    public void registerLoadConditions(Register<MapCodec<? extends ILoadCondition>> registry) {
        registry.add(And.TYPE_ID, And.CODEC);
        registry.add(Not.TYPE_ID, Not.CODEC);
        registry.add(Or.TYPE_ID, Or.CODEC);
        registry.add(OnPlatform.TYPE_ID, OnPlatform.CODEC);
        registry.add(ModLoaded.TYPE_ID, ModLoaded.CODEC);
        registry.add(RegistryContains.BLOCK, RegistryContains.of(RegistryContains.BLOCK, BuiltInRegistries.BLOCK));
        registry.add(RegistryContains.ITEM, RegistryContains.of(RegistryContains.ITEM, BuiltInRegistries.ITEM));
        registry.add(RegistryContains.ENTITY, RegistryContains.of(RegistryContains.ENTITY, BuiltInRegistries.ENTITY_TYPE));
        registry.add(RegistryContains.BLOCK_ENTITY, RegistryContains.of(RegistryContains.BLOCK_ENTITY, BuiltInRegistries.BLOCK_ENTITY_TYPE));
    }

    @Override
    public void registerItemSubPredicates(Register<ItemSubPredicate.Type<?>> registry) {
        registry.add("namespace", new ItemSubPredicate.Type<>(NamespaceItemPredicate.CODEC));
    }

    @Override
    public void registerCriteriaTriggers(Register<CriterionTrigger<?>> registry) {
        registry.add("earn_advancement", AdvancementTrigger.TRIGGER);
    }

    @Override
    public void registerLootEntryType(Register<MapCodec<? extends LootPoolEntryContainer>> register) {
        register.add("item_stack", LootItemStack.CODEC);
    }

    @Override
    public void registerLootDescriptions(RegisterLootDescription registry) {
        registry.registryFunc().accept(LootPoolEntries.EMPTY, LootPoolEntryDescriptions.EMPTY);
        registry.registryFunc().accept(LootPoolEntries.ITEM, LootPoolEntryDescriptions.ITEM);
        registry.registryFunc().accept(LootPoolEntries.LOOT_TABLE, LootPoolEntryDescriptions.LOOT_TABLE);
        registry.registryFunc().accept(LootPoolEntries.DYNAMIC, LootPoolEntryDescriptions.DYNAMIC);
        registry.registryFunc().accept(LootPoolEntries.TAG, LootPoolEntryDescriptions.TAG);
        registry.registryFunc().accept(LootPoolEntries.ALTERNATIVES, LootPoolEntryDescriptions.COMPOSITE);
        registry.registryFunc().accept(LootPoolEntries.SEQUENCE, LootPoolEntryDescriptions.COMPOSITE);
        registry.registryFunc().accept(LootPoolEntries.GROUP, LootPoolEntryDescriptions.COMPOSITE);
        registry.registryFunc().accept(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.get(Constants.id("item_stack")), LootPoolEntryDescriptions.ITEM_STACK);
    }
}
package net.darkhax.bookshelf.common.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.commands.args.FontArgument;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.registry.IContentProvider;
import net.darkhax.bookshelf.common.api.registry.register.ArgumentRegister;
import net.darkhax.bookshelf.common.api.registry.register.Register;
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
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;

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
        dispatcher.register(root);
    }

    @Override
    public void registerCommandArguments(ArgumentRegister register) {
        register.accept("font", FontArgument.class, FontArgument.SERIALIZER);
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
}
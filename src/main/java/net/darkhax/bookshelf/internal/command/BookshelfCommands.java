package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BookshelfCommands {

    public BookshelfCommands (RegistryHelper registry) {

        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bookshelf");
        new CommandHand(root);
        new CommandTranslate(root);
        new CommandLootChest(root);
        registry.commands.registerCommand(root);
    }
}
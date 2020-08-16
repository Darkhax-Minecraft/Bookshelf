package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class BookshelfCommands {
    
    public BookshelfCommands(RegistryHelper registry) {
        
        final LiteralArgumentBuilder<CommandSource> root = Commands.literal("bookshelf");
        new CommandHand(root);
        new CommandTranslate(root);
        registry.commands.registerCommand(root);
    }
}
package net.darkhax.bookshelf.internal.command;

import java.io.File;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class BookshelfCommands {
    
    private static final String LOOT_TABLE_BLOCK_DROP_SELF = "{\"type\":\"minecraft:block\",\"pools\":[{\"rolls\":1,\"entries\":[{\"type\":\"minecraft:item\",\"name\":\"%output%\"}],\"conditions\":[{\"condition\":\"minecraft:survives_explosion\"}]}]}";
    private static final File OUTPUT_DIR = new File("dump/bookshelf");
    
    public BookshelfCommands(RegistryHelper registry) {
        
        final LiteralArgumentBuilder<CommandSource> root = Commands.literal("bookshelf");
        new CommandHand(root);
        registry.registerCommand(root);
    }
}
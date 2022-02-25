package net.darkhax.bookshelf.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.darkhax.bookshelf.api.registry.ICommandBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BookshelfCommands implements ICommandBuilder {

    @Override
    public void build(CommandDispatcher<CommandSourceStack> dispatcher, boolean isDedicated) {

        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bookshelf");
        root.then(CommandFont.build());

        dispatcher.register(root);
    }
}

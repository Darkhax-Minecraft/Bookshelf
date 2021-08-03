package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

public class CommandTranslate {

    public CommandTranslate (LiteralArgumentBuilder<CommandSourceStack> root) {

        root.then(Commands.literal("translate").then(Commands.argument("key", StringArgumentType.greedyString()).executes(this::translate)));
    }

    private int translate (CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final String translationKey = StringArgumentType.getString(context, "key");
        context.getSource().sendSuccess(new TranslatableComponent(translationKey), false);
        return 0;
    }
}
package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandTranslate {
    
    public CommandTranslate(LiteralArgumentBuilder<CommandSource> root) {
        
        root.then(Commands.literal("translate").then(Commands.argument("key", StringArgumentType.greedyString()).executes(this::translate)));
    }
    
    private int translate (CommandContext<CommandSource> context) throws CommandSyntaxException {
        
        final String translationKey = StringArgumentType.getString(context, "key");
        context.getSource().sendFeedback(new TranslationTextComponent(translationKey), false);
        return 0;
    }
}
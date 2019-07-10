package net.darkhax.bookshelf.command;

import com.mojang.brigadier.context.CommandContext;

import net.darkhax.bookshelf.util.CommandUtils;
import net.minecraft.command.CommandSource;

/**
 * This functional interface allows your commands to have an arbitrary boolean value passed
 * into them without requiring multiple references to be passed into the vanilla command API.
 * This is used with methods such as
 * {@link CommandUtils#createModCommand(String, int, net.darkhax.bookshelf.BookshelfCommands.CommandBoolean)}
 * where the bool value is used to determine if a wildcard value has been specified by the
 * command sender.
 */
@FunctionalInterface
public interface BooleanCommand {
    
    /**
     * Handles the execution of your command.
     * 
     * @param context The context provided by the command.
     * @param bool The arbitrary boolean value.
     * @return A success flag for the command.
     */
    int apply (CommandContext<CommandSource> context, boolean bool);
}

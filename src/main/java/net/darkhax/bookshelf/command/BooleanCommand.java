package net.darkhax.bookshelf.command;

import com.mojang.brigadier.context.CommandContext;

import net.darkhax.bookshelf.util.CommandUtils;
import net.minecraft.commands.CommandSourceStack;

/**
 * This functional interface allows your commands to have an arbitrary boolean value passed
 * into them without requiring multiple references to be passed into the vanilla command API.
 * This is used with {@link CommandUtils#createModCommand(String, int, BooleanCommand)} where
 * the bool value is used to determine if a wildcard value has been specified by the command
 * sender. It can also be used for similar purposes in other types of commands.
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
    int apply (CommandContext<CommandSourceStack> context, boolean bool);
}

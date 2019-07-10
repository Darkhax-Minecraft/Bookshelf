package net.darkhax.bookshelf.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.command.BooleanCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;

public class CommandUtils {
    
    /**
     * Creates a basic command that has no special arguments.
     * 
     * @param key The key for the command.
     * @param permissions The permission level to use the command.
     * @param command The code to execute.
     * @return A LiteralArgumentBuilder that can be registered with a command dispatcher.
     */
    public static LiteralArgumentBuilder<CommandSource> createCommand (String key, int permissions, Command<CommandSource> command) {
        
        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).executes(command);
    }
    
    /**
     * Creates a command with a players argument. Players are mapped to the "targets" argument
     * name.
     * 
     * @param key The key for the command.
     * @param permissions The permission level to use the command.
     * @param command The code to execute.
     * @return A LiteralArgumentBuilder that can be registered with a command dispatcher.
     */
    public static LiteralArgumentBuilder<CommandSource> createPlayerCommand (String key, int permissions, Command<CommandSource> command) {
        
        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).then(Commands.argument("targets", EntityArgument.player()).executes(command));
    }
    
    /**
     * Creates a command with an option boolean value.
     * 
     * @param key The key for the command.
     * @param permissions The permission level to use the command.
     * @param command The code to execute.
     * @return A LiteralArgumentBuilder that can be registered with a command dispatcher.
     */
    public static LiteralArgumentBuilder<CommandSource> createModCommand (String key, int permissions, BooleanCommand command) {
        
        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).executes(ctx -> command.apply(ctx, true)).then(Commands.argument("mod", ArgumentTypeMod.INSTACE).executes(ctx -> command.apply(ctx, false)));
    }
}
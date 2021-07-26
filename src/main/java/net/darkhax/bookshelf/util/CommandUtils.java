/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Map;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.command.BooleanCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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

        return Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).executes(command);
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

        return Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).then(Commands.argument("targets", EntityArgument.player()).executes(command));
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

        return Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).executes(ctx -> command.apply(ctx, true)).then(Commands.argument("mod", ArgumentTypeMod.INSTACE).executes(ctx -> command.apply(ctx, false)));
    }

    /**
     * Checks if the command context contains a given argument.
     *
     * @param context The command context.
     * @param arg The argument being searched for.
     * @return Whether or not the argument was present.
     */
    public static boolean hasArgument (CommandContext<?> context, String arg) {

        final Map<String, ?> arguments = ObfuscationReflectionHelper.getPrivateValue(CommandContext.class, context, "arguments");
        return arguments.containsKey(arg);
    }
}
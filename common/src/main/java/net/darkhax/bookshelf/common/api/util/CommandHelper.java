package net.darkhax.bookshelf.common.api.util;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.darkhax.bookshelf.common.api.commands.IEnumCommand;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.darkhax.bookshelf.common.mixin.access.commands.AccessorCommandContext;
import net.minecraft.commands.CommandSourceStack;

public class CommandHelper {

    /**
     * Creates a command with branching paths that represent the values of an enum.
     *
     * @param parent    The name of the root parent command node.
     * @param enumClass The enum class to use.
     * @param <T>       The type of the enum.
     * @return The newly created command node.
     */
    public static <T extends Enum<T> & IEnumCommand> LiteralArgumentBuilder<CommandSourceStack> buildFromEnum(String parent, Class<T> enumClass) {
        final LiteralArgumentBuilder<CommandSourceStack> parentNode = LiteralArgumentBuilder.literal(parent);
        parentNode.requires(getLowestLevel(enumClass));
        buildFromEnum(parentNode, enumClass);
        return parentNode;
    }

    /**
     * Creates branching command paths that represent the values of an enum.
     *
     * @param parent    The parent node to branch off from.
     * @param enumClass The enum class to use.
     * @param <T>       The type of the enum.
     */
    public static <T extends Enum<T> & IEnumCommand> void buildFromEnum(ArgumentBuilder<CommandSourceStack, ?> parent, Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException("Class '" + enumClass.getCanonicalName() + "' is not an enum!");
        }
        for (T enumEntry : enumClass.getEnumConstants()) {
            final LiteralArgumentBuilder<CommandSourceStack> literal = LiteralArgumentBuilder.literal(enumEntry.getCommandName());
            literal.requires(enumEntry.requiredPermissionLevel()).executes(enumEntry);
            parent.then(literal);
        }
    }

    /**
     * Gets the lowest required permission level for an enum command.
     *
     * @param enumClass The enum class to use.
     * @param <T>       The type of the enum.
     * @return The lowest required permission level for an enum command.
     */
    public static <T extends Enum<T> & IEnumCommand> PermissionLevel getLowestLevel(Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalStateException("Class '" + enumClass.getCanonicalName() + "' is not an enum!");
        }
        PermissionLevel level = PermissionLevel.OWNER;
        for (T enumEntry : enumClass.getEnumConstants()) {
            if (enumEntry.requiredPermissionLevel().get() < level.get()) {
                level = enumEntry.requiredPermissionLevel();
            }
        }
        return level;
    }

    public static <T> boolean hasArgument(String argument, CommandContext<T> context) {
        return context instanceof AccessorCommandContext access && access.bookshelf$getArguments().containsKey(argument);
    }
}
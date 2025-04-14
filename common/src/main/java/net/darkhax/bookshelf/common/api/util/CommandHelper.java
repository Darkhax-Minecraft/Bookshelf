package net.darkhax.bookshelf.common.api.util;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darkhax.bookshelf.common.api.commands.IEnumCommand;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.darkhax.bookshelf.common.mixin.access.commands.AccessorCommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

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

    /**
     * @deprecated This only works on Fabric.
     */
    @Deprecated
    public static <T> boolean hasArgument(String argument, CommandContext<T> context) {
        return context instanceof AccessorCommandContext access && access.bookshelf$getArguments().containsKey(argument);
    }

    public static <T, C> boolean hasArgument(String argument, CommandContext<C> context, Class<T> argType) {
        try {
            return context.getArgument(argument, argType) != null;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static <T, C> T getArgument(String argument, CommandContext<C> context, Class<T> argType, Supplier<T> fallback) {
        return hasArgument(argument, context, argType) ? context.getArgument(argument, argType) : fallback.get();
    }

    public static Entity getEntity(String argName, CommandContext<CommandSourceStack> ctx, Supplier<Entity> fallback) throws CommandSyntaxException {
        return CommandHelper.hasArgument(argName, ctx, EntitySelector.class) ? EntityArgument.getEntity(ctx, argName) : fallback.get();
    }

    public static Entity getEntityOrSender(String argName, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return CommandHelper.hasArgument(argName, ctx, EntitySelector.class) ? EntityArgument.getEntity(ctx, argName) : ctx.getSource().getEntity();
    }

    public static boolean getBooleanArg(String argName, CommandContext<CommandSourceStack> ctx, Supplier<Boolean> fallback) {
        return CommandHelper.hasArgument(argName, ctx, Boolean.class) ? BoolArgumentType.getBool(ctx, argName) : fallback.get();
    }

    public static boolean getBooleanArg(String argName, CommandContext<CommandSourceStack> ctx) {
        return getBooleanArg(argName, ctx, () -> false);
    }
}
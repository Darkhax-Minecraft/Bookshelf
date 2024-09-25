package net.darkhax.bookshelf.common.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TranslateCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return Commands.literal("translate").requires(PermissionLevel.GAMEMASTER).then(Commands.argument("key", StringArgumentType.word()).executes(ctx -> {
            ctx.getSource().sendSuccess(() -> Component.translatable(StringArgumentType.getString(ctx, "key")), false);
            return Command.SINGLE_SUCCESS;
        }));
    }
}

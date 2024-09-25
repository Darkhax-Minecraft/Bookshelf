package net.darkhax.bookshelf.common.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class RenameCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return Commands.literal("rename").requires(PermissionLevel.GAMEMASTER).then(Commands.argument("new_name", ComponentArgument.textComponent(context)).executes(ctx -> {
            final Component newName = ComponentArgument.getComponent(ctx, "new_name");
            if (ctx.getSource().getEntity() instanceof LivingEntity living && !living.getMainHandItem().isEmpty()) {
                living.getMainHandItem().set(DataComponents.CUSTOM_NAME, newName);
            }
            return Command.SINGLE_SUCCESS;
        }));
    }
}

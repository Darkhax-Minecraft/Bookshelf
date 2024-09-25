package net.darkhax.bookshelf.common.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.darkhax.bookshelf.common.api.util.CommandHelper;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("enchant").requires(PermissionLevel.GAMEMASTER);
        root.then(Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT)).then(Commands.argument("level", IntegerArgumentType.integer(0)).executes(EnchantCommand::enchantItem)));
        root.then(Commands.argument("target", EntityArgument.entity()).then(Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT)).then(Commands.argument("level", IntegerArgumentType.integer(0)).executes(EnchantCommand::enchantItem))));
        return root;
    }

    private static int enchantItem(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final Entity target = CommandHelper.hasArgument("target", ctx) ? EntityArgument.getEntity(ctx, "target") : ctx.getSource().getEntity();
        final Holder.Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(ctx, "enchantment");
        final int level = IntegerArgumentType.getInteger(ctx, "level");
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.getMainHandItem().isEmpty()) {
                ctx.getSource().sendFailure(Component.translatable("commands.bookshelf.hand.error.not_air"));
                return 0;
            }
            livingTarget.getMainHandItem().enchant(enchantment, level);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
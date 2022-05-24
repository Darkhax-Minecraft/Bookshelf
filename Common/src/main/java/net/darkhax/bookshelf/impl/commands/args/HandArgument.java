package net.darkhax.bookshelf.impl.commands.args;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.darkhax.bookshelf.api.commands.args.EnumArgument;
import net.darkhax.bookshelf.api.commands.args.SingletonArgumentSerializer;
import net.darkhax.bookshelf.impl.commands.CommandHand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class HandArgument extends EnumArgument<CommandHand.OutputType> {

    public static final HandArgument ARGUMENT = new HandArgument();
    public static final SingletonArgumentSerializer<HandArgument> SERIALIZER = SingletonArgumentSerializer.of(ARGUMENT);

    private HandArgument() {

        super(CommandHand.OutputType.class);
    }

    public static CommandHand.OutputType get(CommandContext<CommandSourceStack> context) {

        return context.getArgument("output_type", CommandHand.OutputType.class);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, CommandHand.OutputType> arg() {

        return Commands.argument("output_type", ARGUMENT);
    }
}
package net.darkhax.bookshelf.api.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface ICommandBuilder {

    void build(CommandDispatcher<CommandSourceStack> dispatcher, boolean isDedicated);
}

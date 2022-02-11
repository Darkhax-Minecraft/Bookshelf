package net.darkhax.bookshelf.api.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface ICommandBuilder {

    void build(CommandDispatcher<CommandSourceStack> dispatcher, boolean isDedicated);
}

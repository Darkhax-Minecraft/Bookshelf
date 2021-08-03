package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.darkhax.bookshelf.command.ArgumentTypeLootTable;
import net.darkhax.bookshelf.util.CommandUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemHandlerHelper;

public class CommandLootChest {

    public CommandLootChest (LiteralArgumentBuilder<CommandSourceStack> root) {

        root.requires(s -> s.hasPermission(2)).then(Commands.literal("lootchest").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("table", ArgumentTypeLootTable.INSTACE).executes(this::translate).then(Commands.argument("seed", StringArgumentType.string()).executes(this::translate)))));
    }

    private int translate (CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final ItemStack item = new ItemStack(Items.CHEST);
        final CompoundTag tag = item.getOrCreateTagElement("BlockEntityTag");
        tag.putString("LootTable", ArgumentTypeLootTable.getTableId(context, "table"));

        if (CommandUtils.hasArgument(context, "seed")) {

            tag.putLong("LootTableSeed", StringArgumentType.getString(context, "seed").hashCode());
        }

        for (final Player player : EntityArgument.getPlayers(context, "targets")) {

            ItemHandlerHelper.giveItemToPlayer(player, item.copy());
        }

        return 0;
    }
}
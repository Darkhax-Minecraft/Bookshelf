package net.darkhax.bookshelf.internal.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.darkhax.bookshelf.command.ArgumentTypeLootTable;
import net.darkhax.bookshelf.util.CommandUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemHandlerHelper;

public class CommandLootChest {

    public CommandLootChest (LiteralArgumentBuilder<CommandSource> root) {

        root.requires(s -> s.hasPermission(2)).then(Commands.literal("lootchest").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("table", ArgumentTypeLootTable.INSTACE).executes(this::translate).then(Commands.argument("seed", StringArgumentType.string()).executes(this::translate)))));
    }

    private int translate (CommandContext<CommandSource> context) throws CommandSyntaxException {

        final ItemStack item = new ItemStack(Items.CHEST);
        final CompoundNBT tag = item.getOrCreateTagElement("BlockEntityTag");
        tag.putString("LootTable", ArgumentTypeLootTable.getTableId(context, "table"));

        if (CommandUtils.hasArgument(context, "seed")) {

            tag.putLong("LootTableSeed", StringArgumentType.getString(context, "seed").hashCode());
        }

        for (final PlayerEntity player : EntityArgument.getPlayers(context, "targets")) {

            ItemHandlerHelper.giveItemToPlayer(player, item.copy());
        }

        return 0;
    }
}
package net.darkhax.bookshelf.common.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.darkhax.bookshelf.common.api.commands.IEnumCommand;
import net.darkhax.bookshelf.common.api.commands.PermissionLevel;
import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriptions;
import net.darkhax.bookshelf.common.api.util.CommandHelper;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.darkhax.bookshelf.common.impl.Constants;
import net.darkhax.bookshelf.common.impl.data.loot.modifiers.ILootPoolHooks;
import net.darkhax.bookshelf.common.mixin.access.loot.AccessorLootTable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

public enum DebugCommands implements IEnumCommand {

    MISSING_TAG_NAMES(DebugCommands::findMissingTagNames),
    MISSING_BLOCK_DROPS(DebugCommands::findMissingBlockDrops),
    LOOT_POOL_HASH(DebugCommands::findLootTableHashes),
    SIMPLE_TABLES(DebugCommands::printTables);

    private static void printTables(MinecraftServer server, StringJoiner out) {
        final Collection<ResourceLocation> tableKeys = server.reloadableRegistries().getKeys(Registries.LOOT_TABLE);
        final RegistryAccess registries = server.reloadableRegistries().get();
        for (ResourceLocation tableKey : tableKeys) {
            final LootTable table = server.reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tableKey));
            out.add(tableKey + " = " + LootPoolEntryDescriptions.getUniqueItems(registries, table));
        }
    }

    private static void findMissingTagNames(MinecraftServer server, StringJoiner out) {
        server.registryAccess().registries().forEach(entry -> {
            AtomicBoolean hasLogged = new AtomicBoolean(false);
            entry.value().getTagNames().forEach(tag -> {
                if (!tag.location().getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) && !I18n.exists(TextHelper.getTagName(tag))) {
                    if (!hasLogged.get()) {
                        hasLogged.set(true);
                        out.add("## " + entry.key().location());
                    }
                    out.add("\"" + TextHelper.getTagName(tag) + "\": \"\",");
                }
            });
            if (hasLogged.get()) {
                out.add("");
            }
        });
    }

    private static void findMissingBlockDrops(MinecraftServer server, StringJoiner out) {
        final HolderGetter<LootTable> lootTables = server.reloadableRegistries().lookup().lookup(Registries.LOOT_TABLE).orElseThrow();
        for (Block block : BuiltInRegistries.BLOCK) {
            final Optional<Holder.Reference<LootTable>> result = lootTables.get(block.getLootTable());
            if (result.isEmpty()) {
                final ResourceLocation id = block.getLootTable().location();
                out.add(BuiltInRegistries.BLOCK.getKey(block) + " - " + id + " - data/" + id.getNamespace() + "/loot_table/" + id.getPath());
            }
        }
    }

    private static void findLootTableHashes(MinecraftServer server, StringJoiner out) {
        final HolderGetter<LootTable> lootTables = server.reloadableRegistries().lookup().lookup(Registries.LOOT_TABLE).orElseThrow();
        for (ResourceLocation table : server.reloadableRegistries().getKeys(Registries.LOOT_TABLE)) {
            if (table.getPath().startsWith("chests") || table.getPath().startsWith("dispensers") || table.getPath().startsWith("gameplay") || table.getPath().startsWith("pots") || table.getPath().startsWith("spawners")) {
                out.add("## " + table);
                lootTables.get(ResourceKey.create(Registries.LOOT_TABLE, table)).ifPresent(val -> {
                    if (val.value() instanceof AccessorLootTable accessor) {
                        for (int index = 0; index < accessor.bookshelf$pools().size(); index++) {
                            if (accessor.bookshelf$pools().get(index) instanceof ILootPoolHooks fingerprintable) {
                                out.add("- " + index + " | " + fingerprintable.bookshelf$getHash());
                            }
                        }
                    }
                });
            }
        }
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return CommandHelper.buildFromEnum("debug", DebugCommands.class);
    }

    private final DebugTask debugTask;

    DebugCommands(DebugTask task) {
        this.debugTask = task;
    }

    @Override
    public String getCommandName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        final StringJoiner joiner = new StringJoiner(System.lineSeparator());
        this.debugTask.getDebugOutput(context.getSource().getServer(), joiner);
        Constants.LOG.warn(joiner.toString());
        final String debugInfo = joiner.toString();
        if (debugInfo.isBlank()) {
            context.getSource().sendFailure(Component.translatable("commands.bookshelf.debug.no_info"));
            return 0;
        }
        else if (debugInfo.length() > 10000) {
            context.getSource().sendFailure(Component.translatable("commands.bookshelf.debug.too_long"));
            return 0;
        }
        else {
            context.getSource().sendSuccess(() -> TextHelper.setCopyText(Component.translatable("commands.bookshelf.debug.yes_info"), debugInfo), false);
            return 1;
        }
    }

    @Override
    public PermissionLevel requiredPermissionLevel() {
        return PermissionLevel.OWNER;
    }

    @FunctionalInterface
    public interface DebugTask {
        void getDebugOutput(MinecraftServer server, StringJoiner output);
    }
}

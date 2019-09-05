package net.darkhax.bookshelf.internal.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.CommandUtils;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class BookshelfCommands {
    
    private static final String LOOT_TABLE_BLOCK_DROP_SELF = "{\"type\":\"minecraft:block\",\"pools\":[{\"rolls\":1,\"entries\":[{\"type\":\"minecraft:item\",\"name\":\"%output%\"}],\"conditions\":[{\"condition\":\"minecraft:survives_explosion\"}]}]}";
    private static final File OUTPUT_DIR = new File("dump/bookshelf");
    
    public BookshelfCommands(RegistryHelper registry) {
        
        final LiteralArgumentBuilder<CommandSource> root = Commands.literal("bookshelf");
        root.then(CommandUtils.createModCommand("validateLootTables", 2, this::validateLootTables));
        new CommandHand(root);
        registry.registerCommand(root);
    }
    
    
    private int validateLootTables (CommandContext<CommandSource> context, boolean wildcard) {
        
        final String modId = wildcard ? "every mod" : ArgumentTypeMod.getMod(context, "mod");
        int foundIssues = 0;
        
        for (final Block block : ForgeRegistries.BLOCKS) {
            
            if ((wildcard || block.getRegistryName().getNamespace().equals(modId)) && !WorldUtils.doesLootTableExist(context.getSource().getWorld(), block.getLootTable())) {
                
                final ResourceLocation id = block.getRegistryName();
                final File outputFile = new File(OUTPUT_DIR, "data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
                
                try {
                    
                    FileUtils.forceMkdir(outputFile.getParentFile());
                    
                    try (FileWriter writer = new FileWriter(outputFile)) {
                        
                        writer.write(LOOT_TABLE_BLOCK_DROP_SELF.replace("%output%", id.toString()));
                    }
                }
                
                catch (final IOException e) {
                    
                    Bookshelf.LOG.error("Failed to write dummy block loot table.", e);
                }
                foundIssues++;
            }
        }
        
        context.getSource().sendFeedback(new TranslationTextComponent("commands.bookshelf.loot_tables", foundIssues, modId), false);
        return 0;
    }
}
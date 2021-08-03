package net.darkhax.bookshelf.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;

public class ArgumentTypeLootTable implements ArgumentType<String> {

    /**
     * An instance of this argument type.
     */
    public static final ArgumentTypeLootTable INSTACE = new ArgumentTypeLootTable();

    /**
     * A list of examples used for this type.
     */
    private static final List<String> examples = Arrays.asList("minecraft:chests/simple_dungeon", "modid:table_name", "minecraft:entities/sheep/blue");

    /**
     * Gets a loot table ID from a command context.
     *
     * @param context The context.
     * @param name The name of the parameter/argument.
     * @return The value for this argument.
     */
    public static String getTableId (final CommandContext<?> context, final String name) {

        return context.getArgument(name, String.class);
    }

    @Nullable
    public static ResourceLocation getTable (final CommandContext<?> context, final String name) {

        return ResourceLocation.tryParse(getTableId(context, name));
    }

    @Override
    public String parse (final StringReader reader) throws CommandSyntaxException {

        return ResourceLocation.read(reader).toString();
    }

    @Override
    public String toString () {

        return "string()";
    }

    @Override
    public Collection<String> getExamples () {

        return examples;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions (CommandContext<S> context, SuggestionsBuilder builder) {

        return SharedSuggestionProvider.suggest(BuiltInLootTables.all().stream().map(ResourceLocation::toString), builder);
    }
}
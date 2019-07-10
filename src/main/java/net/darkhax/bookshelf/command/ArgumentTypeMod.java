package net.darkhax.bookshelf.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.ISuggestionProvider;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * A command argument type which depicts a mod id.
 */
public class ArgumentTypeMod implements ArgumentType<String> {
    
    /**
     * An instance of this argument type.
     */
    public static final ArgumentTypeMod INSTACE = new ArgumentTypeMod();
    
    /**
     * A list of examples used for this type.
     */
    private static final List<String> examples = Arrays.asList("moda", "mod_b", "modc");
    
    /**
     * Gets a mod id from a command context.
     *
     * @param context The context.
     * @param name The name of the parameter/argument.
     * @return The value for this argument.
     */
    public static String getMod (final CommandContext<?> context, final String name) {
        
        return context.getArgument(name, String.class);
    }
    
    @Override
    public String parse (final StringReader reader) throws CommandSyntaxException {
        
        return reader.readUnquotedString();
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
        
        final Map<String, ModContainer> map = ObfuscationReflectionHelper.getPrivateValue(ModList.class, ModList.get(), "indexedMods");
        return ISuggestionProvider.suggest(map.keySet(), builder);
    }
    
}

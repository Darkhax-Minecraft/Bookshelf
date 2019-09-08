package net.darkhax.bookshelf.internal.command;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.darkhax.bookshelf.internal.command.CommandHand.OutputType;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;

public class ArgumentTypeHandOutput implements ArgumentType<OutputType> {

    @Override
    public OutputType parse(final StringReader reader) throws CommandSyntaxException {
        
        return Enum.valueOf(OutputType.class, reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        
        return ISuggestionProvider.suggest(Stream.of(OutputType.class.getEnumConstants()).map(Object::toString), builder);
    }

    @Override
    public Collection<String> getExamples() {
        
        return Stream.of(OutputType.class.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    }

    public static class Serialzier implements IArgumentSerializer<ArgumentTypeHandOutput> {

        @Override
        public void write (ArgumentTypeHandOutput argument, PacketBuffer buffer) {
            
        }

        @Override
        public ArgumentTypeHandOutput read (PacketBuffer buffer) {

            return new ArgumentTypeHandOutput();
        }

        @Override
        public void write (ArgumentTypeHandOutput arg, JsonObject json) {
            

        }
    }
}
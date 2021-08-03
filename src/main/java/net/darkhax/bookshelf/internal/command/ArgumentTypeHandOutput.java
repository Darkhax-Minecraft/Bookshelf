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
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class ArgumentTypeHandOutput implements ArgumentType<OutputType> {

    @Override
    public OutputType parse (final StringReader reader) throws CommandSyntaxException {

        return Enum.valueOf(OutputType.class, reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions (final CommandContext<S> context, final SuggestionsBuilder builder) {

        return SharedSuggestionProvider.suggest(Stream.of(OutputType.class.getEnumConstants()).map(Object::toString), builder);
    }

    @Override
    public Collection<String> getExamples () {

        return Stream.of(OutputType.class.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    }

    public static class Serialzier implements ArgumentSerializer<ArgumentTypeHandOutput> {

        @Override
        public void serializeToNetwork (ArgumentTypeHandOutput argument, FriendlyByteBuf buffer) {

        }

        @Override
        public ArgumentTypeHandOutput deserializeFromNetwork (FriendlyByteBuf buffer) {

            return new ArgumentTypeHandOutput();
        }

        @Override
        public void serializeToJson (ArgumentTypeHandOutput arg, JsonObject json) {

        }
    }
}
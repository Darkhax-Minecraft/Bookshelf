package net.darkhax.bookshelf.command;

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

import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;

public class ArgumentTypeEnum<T extends Enum<T>> implements ArgumentType<T> {
    
    private final Class<T> enumClass;

    public static <R extends Enum<R>> ArgumentTypeEnum<R> enumArgument(Class<R> clazz) {
        
        return new ArgumentTypeEnum<>(clazz);
    }
    
    private ArgumentTypeEnum(Class<T> clazz) {
        
        this.enumClass = clazz;
    }

    @Override
    public T parse(final StringReader reader) throws CommandSyntaxException {
        
        return Enum.valueOf(enumClass, reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        
        return ISuggestionProvider.suggest(Stream.of(enumClass.getEnumConstants()).map(Object::toString), builder);
    }

    @Override
    public Collection<String> getExamples() {
        
        return Stream.of(enumClass.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class Serialzier implements IArgumentSerializer<ArgumentTypeEnum> {

        @Override
        public void write (ArgumentTypeEnum argument, PacketBuffer buffer) {
            
            buffer.writeString(argument.enumClass.getName());
        }

        @Override
        public ArgumentTypeEnum read (PacketBuffer buffer) {

            String className = buffer.readString();
            try {
                return new ArgumentTypeEnum(Class.forName(className));
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void write (ArgumentTypeEnum arg, JsonObject json) {
            

            json.addProperty("enumClass", arg.enumClass.getName());
        }
    }
}
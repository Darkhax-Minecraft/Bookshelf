package net.darkhax.bookshelf.api.commands.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EnumArgument<T extends Enum<T>> implements ArgumentType<T> {

    private static final DynamicCommandExceptionType ERROR_UNKNOWN = new DynamicCommandExceptionType((obj) -> Component.translatable("argument.bookshelf.unknown_value", obj));

    private final CachedSupplier<Map<String, T>> values;

    public EnumArgument(Class<T> enumClass) {

        values = CachedSupplier.cache(() -> {

            final Map<String, T> valueMap = new HashMap<>();

            for (T enumConst : enumClass.getEnumConstants()) {

                valueMap.put(enumConst.name(), enumConst);
            }

            return valueMap;
        });
    }

    @Override
    public T parse(final StringReader reader) throws CommandSyntaxException {

        final String inputEnumName = reader.readUnquotedString();

        if (values.get().containsKey(inputEnumName)) {

            return values.get().get(inputEnumName);
        }

        throw ERROR_UNKNOWN.createWithContext(reader, inputEnumName);
    }

    @Override
    public Collection<String> getExamples() {

        return this.values.get().keySet();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        return SharedSuggestionProvider.suggest(this.values.get().keySet(), builder);
    }
}
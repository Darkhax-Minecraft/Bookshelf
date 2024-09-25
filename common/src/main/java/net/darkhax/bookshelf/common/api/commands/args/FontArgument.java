package net.darkhax.bookshelf.common.api.commands.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.api.text.font.BuiltinFonts;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FontArgument implements ArgumentType<ResourceLocation> {

    public static final FontArgument ARGUMENT = new FontArgument();
    public static final ArgumentTypeInfo<FontArgument, ?> SERIALIZER = SingletonArgumentInfo.of(() -> ARGUMENT);
    private static final Set<String> EXAMPLES = Set.of(BuiltinFonts.DEFAULT.identifier().toString(), BuiltinFonts.ALT.identifier().toString(), BuiltinFonts.ILLAGER.identifier().toString());

    public static ResourceLocation get(CommandContext<CommandSourceStack> context) {
        return get("font", context);
    }

    public static ResourceLocation get(String argName, CommandContext<CommandSourceStack> context) {
        return context.getArgument(argName, ResourceLocation.class);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> argument() {
        return argument("font");
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> argument(String argName) {
        return Commands.argument(argName, ARGUMENT);
    }

    @Override
    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (Services.PLATFORM.isPhysicalClient()) {
            return SharedSuggestionProvider.suggestResource(TextHelper.getRegisteredFonts(), builder);
        }
        return SharedSuggestionProvider.suggestResource(BuiltinFonts.FONT_IDS, builder);
    }
}
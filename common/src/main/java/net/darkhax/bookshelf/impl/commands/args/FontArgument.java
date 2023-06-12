package net.darkhax.bookshelf.impl.commands.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.commands.args.SingletonArgumentInfo;
import net.darkhax.bookshelf.api.util.TextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FontArgument implements ArgumentType<ResourceLocation> {

    public static final FontArgument ARGUMENT = new FontArgument();
    public static final ArgumentTypeInfo<FontArgument, ?> FONT_SERIALIZER = SingletonArgumentInfo.of(ARGUMENT);

    private static final Collection<ResourceLocation> VANILLA_FONTS = List.of(TextHelper.FONT_DEFAULT, TextHelper.FONT_ALT, TextHelper.FONT_UNIFORM, TextHelper.FONT_ILLAGER);
    private static final Collection<String> EXAMPLES = VANILLA_FONTS.stream().map(ResourceLocation::toString).collect(Collectors.toList());

    public static ResourceLocation getFont(CommandContext<CommandSourceStack> context) {

        return context.getArgument("font", ResourceLocation.class);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> argument() {

        return Commands.argument("font", ARGUMENT);
    }

    @Override
    public ResourceLocation parse(final StringReader reader) throws CommandSyntaxException {

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

        return SharedSuggestionProvider.suggestResource(VANILLA_FONTS, builder);
    }
}
package net.darkhax.bookshelf.common.api.commands.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TagArgument<T> implements ArgumentType<TagKey<T>> {

    private static final MapCodec<ResourceKey<? extends Registry<?>>> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("registry_name").forGetter(ResourceKey::location)
    ).apply(instance, ResourceKey::createRegistryKey));
    private static final StreamCodec<FriendlyByteBuf, ResourceKey<? extends Registry<?>>> STREAM = StreamCodec.of(
            (buf, key) -> buf.writeResourceLocation(key.location()),
            buf -> ResourceKey.createRegistryKey(buf.readResourceLocation())
    );
    public static final ArgumentSerializer<TagArgument<?>, ResourceKey<? extends Registry<?>>> SERIALIZER = new ArgumentSerializer<>(CODEC, STREAM, TagArgument::makeRaw, t -> t.registryKey);
    private static final Collection<String> EXAMPLES = Arrays.asList("minecraft:dirt", "minecraft:axolotl_food", "minecraft:enchantable/bow");

    private final HolderLookup<T> registryLookup;
    private final ResourceKey<? extends Registry<T>> registryKey;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static TagArgument<?> makeRaw(CommandBuildContext context, ResourceKey registryKey) {
        return new TagArgument<>(context, registryKey);
    }

    private TagArgument(CommandBuildContext context, ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
        this.registryLookup = context.lookupOrThrow(registryKey);
    }

    @Override
    public TagKey<T> parse(StringReader reader) throws CommandSyntaxException {
        final ResourceLocation tagId = ResourceLocation.read(reader);
        return TagKey.create(this.registryKey, tagId);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(this.registryLookup.listTagIds().map(TagKey::location), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static <T> TagArgument<T> arg(CommandBuildContext context, ResourceKey<? extends Registry<T>> registry) {
        return new TagArgument<>(context, registry);
    }

    @SuppressWarnings("unchecked")
    public static <T> TagKey<T> get(String argName, CommandContext<CommandSourceStack> context, ResourceKey<? extends Registry<T>> registry) {
        return (TagKey<T>) context.getArgument(argName, TagKey.class);
    }
}

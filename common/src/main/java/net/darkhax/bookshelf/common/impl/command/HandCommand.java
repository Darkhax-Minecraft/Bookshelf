package net.darkhax.bookshelf.common.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.darkhax.bookshelf.common.api.commands.IEnumCommand;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.util.CommandHelper;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum HandCommand implements IEnumCommand {

    ID((stack, level) -> TextHelper.copyText(Objects.requireNonNull(level.registryAccess().registryOrThrow(Registries.ITEM).getKey(stack.getItem())).toString())),
    STRING((stack, level) -> TextHelper.copyText(stack.toString())),
    INGREDIENT(json(MapCodecs.INGREDIENT.get(), (stack, level) -> Ingredient.of(stack))),
    STACK_JSON(json(MapCodecs.ITEM_STACK.get(), (stack, level) -> stack)),
    STACK_NBT(nbt(MapCodecs.ITEM_STACK.get(), (stack, level) -> stack)),
    COMPONENTS((stack, level) -> {
        final StringJoiner joiner = new StringJoiner("\n");
        stack.getComponents().stream().sorted(Comparator.comparing(r -> r.type().toString())).forEach(component -> {
            joiner.add(component.type() + " = " + unsafeEncode(Objects.requireNonNull(component.type().codec()), NbtOps.INSTANCE, component.value(), level));
        });
        return TextHelper.copyText(joiner.toString());
    }),
    TAGS(((stack, level) -> {
        final StringJoiner joiner = new StringJoiner("\n");
        stack.getTags().map(key -> key.location().toString()).sorted().forEach(joiner::add);
        return TextHelper.copyText(joiner.toString());
    }));

    private final ItemFormat format;

    HandCommand(ItemFormat format) {
        this.format = format;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof LivingEntity living) {
            context.getSource().sendSuccess(() -> getFormattedResults(context.getSource().getLevel(), living.getMainHandItem()), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private Component getFormattedResults(ServerLevel level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Component.translatable("commands.bookshelf.hand.error.not_air").withStyle(ChatFormatting.RED);
        }
        try {
            return this.format.formatItem(stack, level);
        }
        catch (Throwable e) {
            Constants.LOG.error("Encountered an error when formatting item as {}.", this.name(), e);
        }
        return Component.translatable("commands.bookshelf.hand.error.internal").withStyle(ChatFormatting.RED);
    }

    private static <T> ItemFormat json(Codec<T> codec, BiFunction<ItemStack, ServerLevel, T> mapper) {
        return fromCodec(JsonOps.INSTANCE, Constants.GSON_PRETTY::toJson, codec, mapper);
    }

    private static <T> ItemFormat nbt(Codec<T> codec, BiFunction<ItemStack, ServerLevel, T> mapper) {
        return fromCodec(NbtOps.INSTANCE, Tag::toString, codec, mapper);
    }

    private static <T, D> ItemFormat fromCodec(DynamicOps<D> ops, Function<D, String> dataFormatter, Codec<T> codec, BiFunction<ItemStack, ServerLevel, T> mapper) {
        return (stack, level) -> {
            final T value = mapper.apply(stack, level);
            final D data = codec.encodeStart(level.registryAccess().createSerializationContext(ops), value).getOrThrow();
            return TextHelper.copyText(dataFormatter.apply(data));
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> T unsafeEncode(Codec codec, DynamicOps<T> ops, Object input, ServerLevel level) {
        return (T) codec.encodeStart(level.registryAccess().createSerializationContext(ops), input).getOrThrow();
    }

    @Override
    public String getCommandName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return CommandHelper.buildFromEnum("hand", HandCommand.class);
    }

    interface ItemFormat {
        Component formatItem(ItemStack stack, ServerLevel level);
    }
}
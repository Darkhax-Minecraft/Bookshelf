package net.darkhax.bookshelf.common.impl.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.darkhax.bookshelf.common.api.commands.IEnumCommand;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.util.CommandHelper;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Locale;
import java.util.function.BiFunction;

public enum HandCommand implements IEnumCommand {

    STRING((stack, level) -> TextHelper.copyText(stack.toString())),
    INGREDIENT(fromCodec(MapCodecs.INGREDIENT.get(), (stack, level) -> Ingredient.of(stack))),
    STACK_JSON(fromCodec(MapCodecs.ITEM_STACK.get(), (stack, level) -> stack));

    private final ItemFormat format;

    HandCommand(ItemFormat format) {
        this.format = format;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof LivingEntity living) {
            context.getSource().sendSuccess(() -> this.format.formatItem(living.getMainHandItem(), source.getLevel()), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static <T> ItemFormat fromCodec(Codec<T> codec, BiFunction<ItemStack, ServerLevel, T> mapper) {
        return (stack, level) -> {
            if (stack.isEmpty()) {
                return Component.translatable("commands.bookshelf.hand.error.not_air").withStyle(ChatFormatting.RED);
            }
            final T value = mapper.apply(stack, level);
            final JsonElement json = codec.encodeStart(RegistryOps.create(JsonOps.INSTANCE, level.registryAccess()), value).getOrThrow();
            return TextHelper.copyText(Constants.GSON_PRETTY.toJson(json));
        };
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
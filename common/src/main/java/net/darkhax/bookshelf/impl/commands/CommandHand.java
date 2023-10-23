package net.darkhax.bookshelf.impl.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.bookshelf.api.util.TextHelper;
import net.darkhax.bookshelf.impl.commands.args.HandArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.StringJoiner;
import java.util.function.Function;

public class CommandHand {

    public static LiteralArgumentBuilder<CommandSourceStack> build() {

        final LiteralArgumentBuilder<CommandSourceStack> hand = Commands.literal("hand");
        hand.then(HandArgument.arg().executes(CommandHand::printHeldStack));
        return hand;
    }

    private static int printHeldStack(CommandContext<CommandSourceStack> context) {

        final OutputType type = HandArgument.get(context);
        final Entity sender = context.getSource().getEntity();

        if (sender instanceof LivingEntity living) {

            final String stackOutput = type.converter.apply(living.getMainHandItem());
            context.getSource().sendSuccess(() -> TextHelper.textWithCopy(stackOutput), false);
        }

        return 1;
    }

    public enum OutputType {

        STRING(OutputType::getAsString),
        INGREDIENT(OutputType::getAsIngredient),
        STACK_JSON(OutputType::getAsStackJson),
        SNBT(OutputType::getAsSNBT),
        ID(OutputType::getAsID),
        TAGS(OutputType::getTags);

        private final Function<ItemStack, String> converter;

        OutputType(Function<ItemStack, String> converter) {

            this.converter = converter;
        }

        private static String getAsString(ItemStack stack) {

            return stack.toString();
        }

        private static String getAsIngredient(ItemStack stack) {

            return Serializers.INGREDIENT.toJSON(Ingredient.of(stack)).toString();
        }

        private static String getAsStackJson(ItemStack stack) {

            return Serializers.ITEM_STACK.toJSON(stack).toString();
        }

        public static String getAsSNBT(ItemStack stack) {

            return stack.save(new CompoundTag()).getAsString();
        }

        public static String getAsID(ItemStack stack) {

            final ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            return id != null ? id.toString() : null;
        }

        public static String getTags(ItemStack stack) {

            final StringJoiner joiner = new StringJoiner("\n");

            stack.getItem().builtInRegistryHolder().tags().forEach(tag -> joiner.add(tag.location().toString()));

            return joiner.toString();
        }
    }
}
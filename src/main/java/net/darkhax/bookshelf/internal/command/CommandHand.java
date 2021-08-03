package net.darkhax.bookshelf.internal.command;

import java.util.StringJoiner;
import java.util.function.Function;

import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class CommandHand {

    public CommandHand (LiteralArgumentBuilder<CommandSourceStack> root) {

        root.then(Commands.literal("hand").then(Commands.argument("type", new ArgumentTypeHandOutput()).executes(this::hand)));
    }

    private int hand (CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final OutputType type = context.getArgument("type", OutputType.class);

        final ServerPlayer player = context.getSource().getPlayerOrException();
        final String outputText = type.converter.apply(player.getMainHandItem());

        final Component component = ComponentUtils.wrapInSquareBrackets(new TextComponent(outputText).withStyle( (style) -> {
            return style.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, outputText)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.copy.click"))).withInsertion(outputText);
        }));

        context.getSource().sendSuccess(component, false);

        return 0;
    }

    public enum OutputType {

        STRING(OutputType::getAsString),
        INGREDIENT(OutputType::getAsIngredient),
        STACKJSON(OutputType::getAsStackJson),
        ID(OutputType::getAsID),
        HOLDER(OutputType::getAsHolder),
        TAGS(OutputType::getTags),
        FLUIDS(OutputType::getFluids);

        private final Function<ItemStack, String> converter;

        OutputType (Function<ItemStack, String> converter) {

            this.converter = converter;
        }

        private static String getAsString (ItemStack stack) {

            return stack.toString();
        }

        private static String getAsIngredient (ItemStack stack) {

            final JsonObject json = new JsonObject();
            json.addProperty("type", CraftingHelper.getID(stack.hasTag() ? NBTIngredient.Serializer.INSTANCE : VanillaIngredientSerializer.INSTANCE).toString());
            json.addProperty("item", stack.getItem().getRegistryName().toString());
            json.addProperty("count", stack.getCount());

            if (stack.hasTag()) {

                json.addProperty("nbt", stack.getTag().toString());
            }

            return json.toString();
        }

        private static String getAsStackJson (ItemStack stack) {

            return Serializers.ITEMSTACK.write(stack).toString();
        }

        public static String getAsID (ItemStack stack) {

            return stack.getItem().getRegistryName().toString();
        }

        public static String getAsHolder (ItemStack stack) {

            final ResourceLocation itemId = stack.getItem().getRegistryName();
            return "@ObjectHolder(\"" + itemId.toString() + "\")\npublic static final Item " + itemId.getPath().toUpperCase() + " = null;";
        }

        public static String getTags (ItemStack stack) {

            final StringJoiner joiner = new StringJoiner("\n");

            for (final ResourceLocation tag : stack.getItem().getTags()) {

                joiner.add(tag.toString());
            }

            return joiner.toString();
        }

        public static String getFluids (ItemStack stack) {

            final StringJoiner joiner = new StringJoiner("\n");

            final LazyOptional<IFluidHandlerItem> fluidCap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

            fluidCap.ifPresent(fluidHandler -> {

                for (int i = 0; i < fluidHandler.getTanks(); i++) {

                    final FluidStack fluidStack = fluidHandler.getFluidInTank(i);

                    if (!fluidStack.isEmpty()) {

                        joiner.add(fluidStack.getRawFluid().getRegistryName().toString());
                    }
                }
            });

            return joiner.toString();
        }
    }
}
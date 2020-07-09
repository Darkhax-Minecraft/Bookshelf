package net.darkhax.bookshelf.internal.command;

import java.util.function.Function;

import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;

public class CommandHand {
    
    public CommandHand(LiteralArgumentBuilder<CommandSource> root) {
        
        root.then(Commands.literal("hand").then(Commands.argument("type", new ArgumentTypeHandOutput()).executes(this::hand)));
    }
    
    private int hand (CommandContext<CommandSource> context) throws CommandSyntaxException {
        
        final OutputType type = context.getArgument("type", OutputType.class);
        
        final ServerPlayerEntity player = context.getSource().asPlayer();
        final String outputText = type.converter.apply(player.getHeldItemMainhand());
        
        final ITextComponent component = TextComponentUtils.func_240647_a_(new StringTextComponent(outputText).func_240700_a_( (style) -> {
            return style.setFormatting(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, outputText)).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click"))).setInsertion(outputText);
        }));
        
        context.getSource().sendFeedback(component, false);
        
        return 0;
    }
    
    public enum OutputType {
        
        STRING(OutputType::getAsString),
        JSON(OutputType::getAsJson),
        ID(OutputType::getAsID),
        HOLDER(OutputType::getAsHolder);
        
        private final Function<ItemStack, String> converter;
        
        OutputType(Function<ItemStack, String> converter) {
            
            this.converter = converter;
        }
        
        private static String getAsString (ItemStack stack) {
            
            return stack.toString();
        }
        
        private static String getAsJson (ItemStack stack) {
            
            final JsonObject json = new JsonObject();
            json.addProperty("type", CraftingHelper.getID(stack.hasTag() ? NBTIngredient.Serializer.INSTANCE : VanillaIngredientSerializer.INSTANCE).toString());
            json.addProperty("item", stack.getItem().getRegistryName().toString());
            json.addProperty("count", stack.getCount());
            
            if (stack.hasTag()) {
                
                json.addProperty("nbt", stack.getTag().toString());
            }
            
            return json.toString();
        }
        
        public static String getAsID (ItemStack stack) {
            
            return stack.getItem().getRegistryName().toString();
        }
        
        public static String getAsHolder (ItemStack stack) {
            
            final ResourceLocation itemId = stack.getItem().getRegistryName();
            return "@ObjectHolder(\"" + itemId.toString() + "\")" + Bookshelf.NEW_LINE + "public static final Item " + itemId.getPath().toUpperCase() + " = null;";
        }
    }
}
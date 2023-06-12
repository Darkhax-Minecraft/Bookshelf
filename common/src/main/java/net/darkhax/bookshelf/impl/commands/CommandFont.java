package net.darkhax.bookshelf.impl.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darkhax.bookshelf.api.util.TextHelper;
import net.darkhax.bookshelf.impl.commands.args.FontArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.function.UnaryOperator;

public class CommandFont {

    public static LiteralArgumentBuilder<CommandSourceStack> build() {

        final LiteralArgumentBuilder<CommandSourceStack> font = Commands.literal("font");

        font.then(Commands.literal("item").requires(p -> p.hasPermission(2)).then(FontArgument.argument().executes(CommandFont::renameItemWithFont)));
        font.then(Commands.literal("block").requires(p -> p.hasPermission(2)).then(FontArgument.argument().then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(CommandFont::renameBlockWithFont))));
        font.then(Commands.literal("book").requires(p -> p.hasPermission(2)).then(FontArgument.argument().executes(CommandFont::setBookFont)));
        font.then(Commands.literal("say").requires(p -> p.hasPermission(2)).then(FontArgument.argument().then(Commands.argument("message", MessageArgument.message()).executes(CommandFont::speakWithFont))));

        return font;
    }

    private static int speakWithFont(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        //TODO Consider adding a message decorator or chat type and use signed chat?
        final ResourceLocation fontId = FontArgument.getFont(context);
        final Component inputMessage = TextHelper.applyFont(MessageArgument.getMessage(context, "message"), fontId);
        final Component txtMessage = Component.translatable("chat.type.announcement", context.getSource().getDisplayName(), inputMessage);
        context.getSource().getServer().getPlayerList().broadcastSystemMessage(txtMessage, false);
        return 0;
    }

    private static int renameItemWithFont(CommandContext<CommandSourceStack> context) {

        final ResourceLocation fontId = FontArgument.getFont(context);
        final Entity sender = context.getSource().getEntity();

        if (sender instanceof LivingEntity living) {

            final ItemStack stack = living.getMainHandItem();
            stack.setHoverName(TextHelper.applyFont(stack.getHoverName(), fontId));
            return 1;
        }

        return 0;
    }

    private static int renameBlockWithFont(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        final ServerLevel world = context.getSource().getLevel();
        final ResourceLocation fontId = FontArgument.getFont(context);
        final BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        final BlockEntity tile = world.getBlockEntity(pos);

        if (tile != null) {

            if (tile instanceof BaseContainerBlockEntity container) {

                container.setCustomName(TextHelper.applyFont(container.getName(), fontId));
            }

            if (tile instanceof SignBlockEntity sign) {

                sign.updateText(applySignFont(fontId), true);
                sign.updateText(applySignFont(fontId), false);
                sign.getLevel().sendBlockUpdated(sign.getBlockPos(), sign.getBlockState(), sign.getBlockState(), 3);
            }
        }

        return 1;
    }

    private static UnaryOperator<SignText> applySignFont(ResourceLocation fontId) {
        return text -> {
            for (int i = 0; i < 4; i++) {
                text.setMessage(i, TextHelper.applyFont(text.getMessage(i, false), fontId));
            }
            return text;
        };
    }

    private static int setBookFont(CommandContext<CommandSourceStack> context) {

        final ResourceLocation fontId = FontArgument.getFont(context);
        final Entity sender = context.getSource().getEntity();

        if (sender instanceof LivingEntity living) {

            final ItemStack stack = living.getMainHandItem();

            if (stack.getItem() instanceof WrittenBookItem book && stack.hasTag()) {

                stack.setHoverName(TextHelper.applyFont(stack.getHoverName(), fontId));

                final CompoundTag stackTag = stack.getTag();

                if (stackTag != null) {

                    final ListTag pageData = stackTag.getList("pages", Tag.TAG_STRING);

                    for (int pageNum = 0; pageNum < pageData.size(); pageNum++) {

                        final Component pageText = Component.Serializer.fromJsonLenient(pageData.getString(pageNum));
                        TextHelper.applyFont(pageText, fontId);
                        pageData.set(pageNum, StringTag.valueOf(Component.Serializer.toJson(pageText)));
                    }

                    stackTag.put("pages", pageData);
                }

                return 1;
            }
        }

        return 0;
    }
}

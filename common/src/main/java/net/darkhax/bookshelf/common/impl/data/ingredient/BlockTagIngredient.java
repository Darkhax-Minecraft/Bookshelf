package net.darkhax.bookshelf.common.impl.data.ingredient;

import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockTagIngredient implements IngredientLogic<BlockTagIngredient> {

    public static final MapCodec<BlockTagIngredient> CODEC = MapCodecs.flexibleList(TagKey.codec(Registries.BLOCK)).xmap(BlockTagIngredient::new, l -> l.blockTags).fieldOf("tag");
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockTagIngredient> STREAM = StreamCodec.of(
            (buf, val) -> {
                buf.writeVarInt(val.blockTags.size());
                for (TagKey<Block> block : val.blockTags) {
                    buf.writeIdentifier(block.location());
                }
            },
            buf -> {
                final int size = buf.readVarInt();
                final List<TagKey<Block>> tags = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    tags.add(TagKey.create(Registries.BLOCK, buf.readIdentifier()));
                }
                return new BlockTagIngredient(tags);
            }
    );

    private final List<TagKey<Block>> blockTags;
    private List<ItemStack> matches;

    public BlockTagIngredient(List<TagKey<Block>> blockTags) {
        this.blockTags = blockTags;
    }

    @Override
    public List<ItemStack> getAllMatchingStacks() {
        if (this.matches == null) {
            this.matches = new ArrayList<>();
            for (TagKey<Block> tag : blockTags) {
                for (Holder<Block> entry : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
                    final ItemStack stack = new ItemStack(entry.value());
                    if (!stack.isEmpty()) {
                        this.matches.add(stack);
                    }
                }
            }
        }
        return this.matches;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        for (ItemStack valid : this.getAllMatchingStacks()) {
            if (valid.getItem() == stack.getItem()) {
                return true;
            }
        }
        return false;
    }
}
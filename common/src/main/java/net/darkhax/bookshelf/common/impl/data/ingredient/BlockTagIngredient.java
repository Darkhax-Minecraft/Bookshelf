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
            (buf, val) -> buf.writeCollection(val.blockTags, (b1, tag) -> {
                b1.writeIdentifier(tag.location());
            }),
            buf -> new BlockTagIngredient(buf.readCollection(ArrayList::new, b1 -> TagKey.create(Registries.BLOCK, b1.readIdentifier())))
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
package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.Tag.Named;
import net.minecraft.resources.ResourceLocation;

public class BlockIngredientCheckTag extends BlockIngredient {

    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "check_tag");
    public static final Serializer SERIALIZER = new Serializer();

    private final Named<Block> tag;
    private List<BlockState> cache;

    public BlockIngredientCheckTag (Named<Block> tag) {

        this.tag = tag;
    }

    @Override
    public boolean test (BlockState t) {

        this.buildCache();
        return this.cache.contains(t);
    }

    @Override
    public Collection<BlockState> getValidStates () {

        this.buildCache();
        return this.cache;
    }

    @Override
    public ResourceLocation getSerializeId () {

        return ID;
    }

    private void buildCache () {

        if (this.cache == null) {

            this.cache = new ArrayList<>();

            for (final Block block : this.tag.getValues()) {

                block.getStateDefinition().getPossibleStates().forEach(this.cache::add);
            }
        }
    }

    static class Serializer implements IBlockIngredientSerializer<BlockIngredientCheckTag> {

        @Override
        public BlockIngredientCheckTag read (JsonElement json) {

            final Named<Block> tag = Serializers.BLOCK_TAG.read(json.getAsJsonObject(), "tag");
            return new BlockIngredientCheckTag(tag);
        }

        @Override
        public JsonElement write (BlockIngredientCheckTag ingredient) {

            final JsonObject obj = new JsonObject();
            obj.add("tag", Serializers.BLOCK_TAG.write(ingredient.tag));
            return obj;
        }

        @Override
        public BlockIngredientCheckTag read (FriendlyByteBuf buf) {

            final Named<Block> tag = Serializers.BLOCK_TAG.read(buf);
            return new BlockIngredientCheckTag(tag);
        }

        @Override
        public void write (FriendlyByteBuf buf, BlockIngredientCheckTag ingredient) {

            Serializers.BLOCK_TAG.write(buf, ingredient.tag);
        }
    }
}
package net.darkhax.bookshelf.crafting.block;

import java.util.Collection;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class BlockIngredientCheckState extends BlockIngredient {

    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "check_state");
    public static final Serializer SERIALIZER = new Serializer();

    private final List<BlockState> validStates;

    public BlockIngredientCheckState (List<BlockState> validStates) {

        this.validStates = validStates;
    }

    @Override
    public boolean test (BlockState t) {

        return this.validStates.contains(t);
    }

    @Override
    public Collection<BlockState> getValidStates () {

        return this.validStates;
    }

    @Override
    public ResourceLocation getSerializeId () {

        return ID;
    }

    static class Serializer implements IBlockIngredientSerializer<BlockIngredientCheckState> {

        @Override
        public BlockIngredientCheckState read (JsonElement json) {

            final List<BlockState> states = Serializers.BLOCK_STATE.readList(json.getAsJsonObject(), "state");
            return new BlockIngredientCheckState(states);
        }

        @Override
        public JsonElement write (BlockIngredientCheckState ingredient) {

            final JsonObject obj = new JsonObject();
            obj.add("state", Serializers.BLOCK_STATE.writeList(ingredient.validStates));
            return obj;
        }

        @Override
        public BlockIngredientCheckState read (FriendlyByteBuf buf) {

            final List<BlockState> states = Serializers.BLOCK_STATE.readList(buf);
            return new BlockIngredientCheckState(states);
        }

        @Override
        public void write (FriendlyByteBuf buf, BlockIngredientCheckState ingredient) {

            Serializers.BLOCK_STATE.writeList(buf, ingredient.validStates);
        }
    }
}
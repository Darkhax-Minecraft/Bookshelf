package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;

public class BlockIngredientAny extends BlockIngredient {

    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "any");
    public static final Serializer SERIALIZER = new Serializer();

    private final List<BlockIngredient> components;
    private Set<BlockState> stateCache;

    public BlockIngredientAny (List<BlockIngredient> components) {

        this.components = components;
    }

    @Override
    public boolean test (BlockState t) {

        return this.components.stream().anyMatch(i -> i.test(t));
    }

    @Override
    public Collection<BlockState> getValidStates () {

        this.buildCache();
        return this.stateCache;
    }

    @Override
    public ResourceLocation getSerializeId () {

        return ID;
    }

    public void buildCache () {

        if (this.stateCache == null) {

            final Set<BlockState> validStates = new HashSet<>();
            this.components.forEach(c -> {

                final Collection<BlockState> childStates = c.getValidStates();
                childStates.forEach(s -> validStates.add(s));
            });
            this.stateCache = validStates;
        }
    }

    static class Serializer implements IBlockIngredientSerializer<BlockIngredientAny> {

        @Override
        public BlockIngredientAny read (JsonElement json) {

            final ArrayList<BlockIngredient> ingredients = new ArrayList<>();

            for (final JsonElement elem : GsonHelper.getAsJsonArray(json.getAsJsonObject(), "ingredients")) {

                ingredients.add(Serializers.BLOCK_INGREDIENT.read(elem));
            }

            return new BlockIngredientAny(ingredients);
        }

        @Override
        public JsonElement write (BlockIngredientAny ingredient) {

            final JsonObject obj = new JsonObject();
            final JsonArray array = new JsonArray();

            for (final BlockIngredient component : ingredient.components) {

                array.add(Serializers.BLOCK_INGREDIENT.write(component));
            }

            obj.add("ingredients", array);
            return obj;
        }

        @Override
        public BlockIngredientAny read (FriendlyByteBuf buf) {

            final List<BlockIngredient> components = Serializers.BLOCK_INGREDIENT.readList(buf);
            return new BlockIngredientAny(components);
        }

        @Override
        public void write (FriendlyByteBuf buf, BlockIngredientAny ingredient) {

            Serializers.BLOCK_INGREDIENT.writeList(ingredient.components);
        }
    }
}
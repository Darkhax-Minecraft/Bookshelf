package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.darkhax.bookshelf.crafting.block.BlockIngredient;
import net.darkhax.bookshelf.crafting.block.IBlockIngredientSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public final class BlockIngredientSerializer implements ISerializer<BlockIngredient> {

    public static final ISerializer<BlockIngredient> SERIALIZER = new BlockIngredientSerializer();

    private BlockIngredientSerializer () {

    }

    @Override
    public BlockIngredient read (JsonElement json) {

        final JsonObject obj = json.getAsJsonObject();
        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(obj.get("type"));

        final IBlockIngredientSerializer<?> serializer = BlockIngredient.getSerializer(id);

        if (serializer != null) {

            return serializer.read(obj);
        }

        throw new JsonParseException("Block ingredient type " + id + " could not be found!");
    }

    @Override
    public BlockIngredient read (FriendlyByteBuf buf) {

        final ResourceLocation id = Serializers.RESOURCE_LOCATION.read(buf);
        final IBlockIngredientSerializer<?> serializer = BlockIngredient.getSerializer(id);

        if (serializer != null) {

            return serializer.read(buf);
        }

        throw new IllegalStateException("Block ingredient type " + id + " could not be found!");
    }

    @Override
    public JsonElement write (BlockIngredient ingredient) {

        final JsonElement element = this.writeUnsafe(ingredient);

        if (element.isJsonObject()) {

            final JsonObject obj = element.getAsJsonObject();
            obj.add("type", Serializers.RESOURCE_LOCATION.write(ingredient.getSerializeId()));
        }

        else {

            throw new IllegalStateException("Ingredient serializer " + ingredient.getSerializeId() + " returned " + element.getClass().getSimpleName() + " but expected " + JsonObject.class.getSimpleName() + ". This is a mod issue that needs to be fixed.");
        }

        return element;
    }

    @Override
    public void write (FriendlyByteBuf buf, BlockIngredient ingredient) {

        Serializers.RESOURCE_LOCATION.write(buf, ingredient.getSerializeId());
        this.writeUnsafe(buf, ingredient);
    }

    @SuppressWarnings("unchecked")
    private <T extends BlockIngredient> JsonElement writeUnsafe (T ingredient) {

        final IBlockIngredientSerializer<T> serializer = (IBlockIngredientSerializer<T>) BlockIngredient.getSerializer(ingredient.getSerializeId());
        return serializer.write(ingredient);
    }

    @SuppressWarnings("unchecked")
    private <T extends BlockIngredient> void writeUnsafe (FriendlyByteBuf buf, T ingredient) {

        final IBlockIngredientSerializer<T> serializer = (IBlockIngredientSerializer<T>) BlockIngredient.getSerializer(ingredient.getSerializeId());
        serializer.write(buf, ingredient);
    }
}
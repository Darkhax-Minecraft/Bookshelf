package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class SerializerItemStack implements ISerializer<ItemStack> {

    public static final ISerializer<ItemStack> SERIALIZER = new SerializerItemStack();

    private SerializerItemStack() {

    }

    @Override
    public ItemStack fromJSON(JsonElement json) {

        if (json instanceof JsonObject obj) {

            // Our code needs to support deserializing air and empty stacks which is not supported by
            // the underlying deserialization code. To avoid this we check and return early.
            final ResourceLocation identifier = new ResourceLocation(GsonHelper.getAsString(obj, "item"));

            if (BuiltInRegistries.ITEM.get(identifier) == Items.AIR) {

                return ItemStack.EMPTY;
            }

            final ItemStack stack = CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.parse(JsonOps.INSTANCE, obj).get().orThrow();

            if (obj.has("nbt") && !stack.hasTag()) {

                stack.setTag(Serializers.COMPOUND_TAG.fromJSON(obj.get("nbt")));
            }

            return stack;
        }

        else if (json.isJsonPrimitive()) {

            return new ItemStack(Serializers.ITEM.fromJSON(json));
        }

        throw new JsonParseException("Expected JSON object, got " + GsonHelper.getType(json));
    }

    @Override
    public JsonElement toJSON(ItemStack toWrite) {

        final JsonObject json = new JsonObject();

        json.add("item", Serializers.ITEM.toJSON(toWrite.getItem()));
        json.addProperty("count", toWrite.getCount());

        if (toWrite.hasTag()) {

            json.add("nbt", Serializers.COMPOUND_TAG.toJSON(toWrite.getTag()));
        }

        return json;
    }

    @Override
    public ItemStack fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readItem();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, ItemStack toWrite) {

        buffer.writeItem(toWrite);
    }

    @Override
    public Tag toNBT(ItemStack toWrite) {

        return toWrite.save(new CompoundTag());
    }

    @Override
    public ItemStack fromNBT(Tag nbt) {

        if (nbt instanceof CompoundTag compound) {

            return ItemStack.of(compound);
        }

        else if (nbt instanceof StringTag string) {

            return new ItemStack(Serializers.ITEM.fromNBT(string));
        }

        throw new NBTParseException("Expected NBT to be a compound tag or string. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}
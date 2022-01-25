package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Optional;
import java.util.UUID;

public class SerializerAttributeModifier implements ISerializer<AttributeModifier> {

    public static final SerializerAttributeModifier SERIALIZER = new SerializerAttributeModifier();

    private SerializerAttributeModifier() {

    }

    @Override
    public AttributeModifier fromJSON(JsonElement json) {

        if (json instanceof JsonObject obj) {

            final Optional<UUID> id = Serializers.UUID.fromJSONOptional(obj, "uuid");
            final String name = Serializers.STRING.fromJSON(obj, "name");
            final double amount = Serializers.DOUBLE.fromJSON(obj, "amount");
            final AttributeModifier.Operation operation = Serializers.ATTRIBUTE_OPERATION.fromJSON(obj, "operation");

            if (id.isPresent()) {

                return new AttributeModifier(id.get(), name, amount, operation);
            }

            return new AttributeModifier(name, amount, operation);
        }

        throw new JsonParseException("Expected JSON object.");
    }

    @Override
    public JsonElement toJSON(AttributeModifier toWrite) {

        final JsonObject json = new JsonObject();
        json.add("uuid", Serializers.UUID.toJSON(toWrite.getId()));
        json.add("name", Serializers.STRING.toJSON(toWrite.getName()));
        json.add("amount", Serializers.DOUBLE.toJSON(toWrite.getAmount()));
        json.add("operation", Serializers.ATTRIBUTE_OPERATION.toJSON(toWrite.getOperation()));
        return json;
    }

    @Override
    public AttributeModifier fromByteBuf(FriendlyByteBuf buffer) {

        final UUID uuid = Serializers.UUID.fromByteBuf(buffer);
        final String name = Serializers.STRING.fromByteBuf(buffer);
        final double amount = Serializers.DOUBLE.fromByteBuf(buffer);
        final AttributeModifier.Operation operation = Serializers.ATTRIBUTE_OPERATION.fromByteBuf(buffer);
        return new AttributeModifier(uuid, name, amount, operation);
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, AttributeModifier toWrite) {

        Serializers.UUID.toByteBuf(buffer, toWrite.getId());
        Serializers.STRING.toByteBuf(buffer, toWrite.getName());
        Serializers.DOUBLE.toByteBuf(buffer, toWrite.getAmount());
        Serializers.ATTRIBUTE_OPERATION.toByteBuf(buffer, toWrite.getOperation());
    }

    @Override
    public Tag toNBT(AttributeModifier toWrite) {

        return toWrite.save();
    }

    @Override
    public AttributeModifier fromNBT(Tag nbt) {

        return AttributeModifier.load(Serializers.COMPOUND_TAG.fromNBT(nbt));
    }
}
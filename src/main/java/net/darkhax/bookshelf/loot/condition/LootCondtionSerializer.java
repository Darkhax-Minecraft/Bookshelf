package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;

public interface LootCondtionSerializer<T extends ILootCondition> extends ILootSerializer<T> {
    public String getName();


    public void serialize (JsonObject json, T value, JsonSerializationContext context);
    public T deserialize (JsonObject json, JsonDeserializationContext context);

    @Override
    default public void func_230424_a_(JsonObject json, T value, JsonSerializationContext context) {
        serialize(json, value, context);
    }

    @Override
    default public T func_230423_a_(JsonObject json, JsonDeserializationContext context) {
        return deserialize(json, context);
    }

    void setType(LootConditionType lcType);
}

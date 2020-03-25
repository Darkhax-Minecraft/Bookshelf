package net.darkhax.bookshelf.loot.modifier;

import java.util.function.Function;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;

public class SerializerFactory<T extends IGlobalLootModifier> extends GlobalLootModifierSerializer<T> {
    
    private final Function<ILootCondition[], T> factory;
    
    public SerializerFactory(Function<ILootCondition[], T> factory) {
        
        this.factory = factory;
    }
    
    @Override
    public T read (ResourceLocation location, JsonObject data, ILootCondition[] conditions) {
        
        return this.factory.apply(conditions);
    }
}
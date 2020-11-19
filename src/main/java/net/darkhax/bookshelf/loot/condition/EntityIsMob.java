package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.entity.monster.IMob;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootContext.EntityTarget;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

/**
 * This condition checks if a mob is an instance of the IMob type.
 */
public class EntityIsMob implements ILootCondition {
    
    public static final Serializer SERIALIZER = new Serializer();
    
    private final EntityTarget target;
    
    private EntityIsMob(EntityTarget target) {
        
        this.target = target;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        return ctx.get(this.target.getParameter()) instanceof IMob;
    }
    
    @Override
    public LootConditionType func_230419_b_ () {
        
        return Bookshelf.instance.conditionIsMob;
    }
    
    private static class Serializer implements ILootSerializer<EntityIsMob> {
        
        protected Serializer() {
            
        }
        
        @Override
        public void serialize (JsonObject json, EntityIsMob value, JsonSerializationContext context) {
            
            json.add("entity", context.serialize(value.target));
        }
        
        @Override
        public EntityIsMob deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new EntityIsMob(JSONUtils.deserializeClass(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}
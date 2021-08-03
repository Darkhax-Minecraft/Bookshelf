package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * This condition checks if a mob is an instance of the IMob type.
 */
public class EntityIsMob implements LootItemCondition {

    public static final Serializer SERIALIZER = new Serializer();

    private final EntityTarget target;

    private EntityIsMob (EntityTarget target) {

        this.target = target;
    }

    @Override
    public boolean test (LootContext ctx) {

        return ctx.getParamOrNull(this.target.getParam()) instanceof Enemy;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionIsMob;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityIsMob> {

        protected Serializer () {

        }

        @Override
        public void serialize (JsonObject json, EntityIsMob value, JsonSerializationContext context) {

            json.add("entity", context.serialize(value.target));
        }

        @Override
        public EntityIsMob deserialize (JsonObject json, JsonDeserializationContext context) {

            return new EntityIsMob(GsonHelper.getAsObject(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}
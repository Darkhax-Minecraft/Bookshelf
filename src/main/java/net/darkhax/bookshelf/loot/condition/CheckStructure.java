package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

/**
 * A loot condition for checking if it is inside a structure.
 */
public class CheckStructure implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    /**
     * The name of the structure.
     */
    private final String structureName;

    /**
     * The structure being checked for. This is lazy loaded by {@link #loadStructure()}.
     */
    private StructureFeature<?> structure;

    public CheckStructure (String structureName) {

        this.structureName = structureName;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);

        if (pos != null && this.loadStructure()) {

            return WorldUtils.isInStructure(ctx.getLevel(), new BlockPos(pos), this.structure);
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckStructure;
    }

    private boolean loadStructure () {

        if (this.structure == null) {

            this.structure = StructureFeature.STRUCTURES_REGISTRY.get(this.structureName);

            if (this.structure == null) {

                Bookshelf.LOG.error("Loot table condition is looking for structure {} which doesn't exist.", this.structureName);
                return false;
            }
        }

        return true;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckStructure> {

        @Override
        public void serialize (JsonObject json, CheckStructure value, JsonSerializationContext context) {

            json.addProperty("structure", value.structureName);
        }

        @Override
        public CheckStructure deserialize (JsonObject json, JsonDeserializationContext context) {

            final String name = GsonHelper.getAsString(json, "structure");
            return new CheckStructure(name);
        }
    }
}
package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This loot condition checks if the block of the loot generation has a certain amount of forge
 * energy stored within it.
 */
public class CheckEnergy implements LootItemCondition {

    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();

    private final Ints energy;

    public CheckEnergy (Ints energy) {

        this.energy = energy;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);

        if (pos != null) {

            final BlockEntity tile = ctx.getLevel().getBlockEntity(new BlockPos(pos));

            if (tile != null) {

                final LazyOptional<IEnergyStorage> energyCap = tile.getCapability(CapabilityEnergy.ENERGY);
                final IEnergyStorage energyStorage = energyCap.orElse(null);

                if (energyStorage != null) {

                    return this.energy.matches(energyStorage.getEnergyStored());
                }
            }
        }

        return false;
    }

    @Override
    public LootItemConditionType getType () {

        return Bookshelf.instance.conditionCheckEnergy;
    }

    static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CheckEnergy> {

        @Override
        public void serialize (JsonObject json, CheckEnergy value, JsonSerializationContext context) {

            json.add("value", value.energy.serializeToJson());
        }

        @Override
        public CheckEnergy deserialize (JsonObject json, JsonDeserializationContext context) {

            return new CheckEnergy(Ints.fromJson(json));
        }
    }
}
package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.loot.LootConditionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This loot condition checks if the block of the loot generation has a certain amount of forge
 * energy stored within it.
 */
public class CheckEnergy implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final IntBound energy;
    
    public CheckEnergy(IntBound energy) {
        
        this.energy = energy;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        
        if (pos != null) {
            
            final TileEntity tile = ctx.getWorld().getTileEntity(pos);
            
            if (tile != null) {
                
                final LazyOptional<IEnergyStorage> energyCap = tile.getCapability(CapabilityEnergy.ENERGY);
                final IEnergyStorage energyStorage = energyCap.orElse(null);
                
                if (energyStorage != null) {
                    
                    return this.energy.test(energyStorage.getEnergyStored());
                }
            }
        }
        
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }

    static class Serializer implements LootCondtionSerializer<CheckEnergy> {
        public LootConditionType lootConditionType = null;

        @Override
        public void setType(LootConditionType lcType) {
            lootConditionType = lcType;
        }

        @Override
        public String getName() {
            return Bookshelf.MOD_ID + ":check_forge_energy";
        }

        @Override
        public void serialize (JsonObject json, CheckEnergy value, JsonSerializationContext context) {
            
            json.add("value", value.energy.serialize());
        }
        
        @Override
        public CheckEnergy deserialize (JsonObject json, JsonDeserializationContext context) {
            
            return new CheckEnergy(IntBound.fromJson(json));
        }
    }
}
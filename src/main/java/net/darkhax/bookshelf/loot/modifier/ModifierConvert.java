package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class ModifierConvert extends LootModifier {
    
    public static final Serializer SERIALIZER = new Serializer();
    
    private final ResourceLocation tableName;
    
    private final float chance;
    
    public ModifierConvert(ILootCondition[] conditions, ResourceLocation table, float chance) {
        
        super(conditions);
        
        this.tableName = table;
        this.chance = chance;
    }
    
    @Override
    protected List<ItemStack> doApply (List<ItemStack> loot, LootContext ctx) {
        
        final List<ItemStack> outputs = NonNullList.create();
        
        final LootTable table = ctx.getWorld().getServer().getLootTableManager().getLootTableFromLocation(this.tableName);
        
        for (final ItemStack item : loot) {
            
            if (MathsUtils.tryPercentage(this.chance)) {
                
                outputs.addAll(table.generate(ctx));
            }
            
            else {
                
                outputs.add(item);
            }
        }
        
        return outputs;
    }
    
    static class Serializer extends GlobalLootModifierSerializer<ModifierConvert> {
        
        @Override
        public ModifierConvert read (ResourceLocation location, JsonObject data, ILootCondition[] conditions) {
            
            final ResourceLocation tableName = ResourceLocation.tryCreate(JSONUtils.getString(data, "table"));
            final float chance = JSONUtils.getFloat(data, "chance", 1f);
            return new ModifierConvert(conditions, tableName, chance);
        }
    }
}
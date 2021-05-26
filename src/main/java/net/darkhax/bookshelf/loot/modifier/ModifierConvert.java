package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
        
        final LootTable table = ctx.getLevel().getServer().getLootTables().get(this.tableName);
        
        for (final ItemStack item : loot) {
            
            if (MathsUtils.tryPercentage(this.chance)) {
                
                outputs.addAll(table.getRandomItems(ctx));
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
            
            final ResourceLocation tableName = ResourceLocation.tryParse(JSONUtils.getAsString(data, "table"));
            final float chance = JSONUtils.getAsFloat(data, "chance", 1f);
            return new ModifierConvert(conditions, tableName, chance);
        }
        
        @Override
        public JsonObject write (ModifierConvert instance) {
            
            final JsonObject json = new JsonObject();
            json.addProperty("table", instance.tableName.toString());
            
            if (instance.chance > 0f) {
                
                json.addProperty("chance", instance.chance);
            }
            
            return json;
        }
    }
}
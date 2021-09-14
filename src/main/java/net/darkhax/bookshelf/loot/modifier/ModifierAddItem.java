package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class ModifierAddItem extends LootModifier {
    
    public static final Serializer SERIALIZER = new Serializer();
    
    private final ItemStack toAdd;
    
    public ModifierAddItem(ILootCondition[] conditions, ItemStack toAdd) {
        
        super(conditions);
        this.toAdd = toAdd;
    }
    
    @Override
    protected List<ItemStack> doApply (List<ItemStack> loot, LootContext ctx) {
        
        loot.add(this.toAdd.copy());
        return loot;
    }
    
    static class Serializer extends GlobalLootModifierSerializer<ModifierAddItem> {
        
        @Override
        public ModifierAddItem read (ResourceLocation location, JsonObject data, ILootCondition[] conditions) {
            
            final ItemStack item = ShapedRecipe.itemFromJson(data.getAsJsonObject("item"));
            return new ModifierAddItem(conditions, item);
        }
        
        @Override
        public JsonObject write (ModifierAddItem instance) {
            
            final JsonObject json = new JsonObject();
            json.add("item", Serializers.ITEMSTACK.write(instance.toAdd));
            return json;
        }
    }
}
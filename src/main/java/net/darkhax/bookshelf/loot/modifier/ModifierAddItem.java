package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
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
            
            final ItemStack item = ShapedRecipe.deserializeItem(data.getAsJsonObject("item"));
            return new ModifierAddItem(conditions, item);
        }
    }
}
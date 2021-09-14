package net.darkhax.bookshelf.loot.modifier;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifier will clear all the generated loot.
 */
public class ModifierClear extends LootModifier {
    
    public static final GlobalLootModifierSerializer<ModifierClear> SERIALIZER = new GlobalLootModifierSerializer<ModifierClear>() {
        
        @Override
        public ModifierClear read (ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            
            return new ModifierClear(ailootcondition);
        }
        
        @Override
        public JsonObject write (ModifierClear instance) {
            
            return new JsonObject();
        }
    };
    
    private ModifierClear(ILootCondition[] conditionsIn) {
        
        super(conditionsIn);
    }
    
    @Override
    protected List<ItemStack> doApply (List<ItemStack> generatedLoot, LootContext context) {
        
        return new ArrayList<>();
    }
}
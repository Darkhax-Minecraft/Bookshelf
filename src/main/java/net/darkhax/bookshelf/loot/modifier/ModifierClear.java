package net.darkhax.bookshelf.loot.modifier;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifier will clear all the generated loot.
 */
public class ModifierClear extends LootModifier {
    
    public ModifierClear(ILootCondition[] conditionsIn) {
        
        super(conditionsIn);
    }
    
    @Override
    protected List<ItemStack> doApply (List<ItemStack> generatedLoot, LootContext context) {
        
        return new ArrayList<>();
    }
}
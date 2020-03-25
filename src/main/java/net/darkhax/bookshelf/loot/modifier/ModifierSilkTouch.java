package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifiers regenerates the loot of a block as if it had silk touch.
 */
public class ModifierSilkTouch extends LootModifier {
    
    public ModifierSilkTouch(ILootCondition[] conditionsIn) {
        
        super(conditionsIn);
    }
    
    @Nonnull
    @Override
    public List<ItemStack> doApply (List<ItemStack> loot, LootContext context) {
        
        final ItemStack tool = context.get(LootParameters.TOOL);
        
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
            
            final ItemStack fakeTool = tool.copy();
            fakeTool.addEnchantment(Enchantments.SILK_TOUCH, 1);
            
            final LootContext.Builder builder = new LootContext.Builder(context);
            builder.withParameter(LootParameters.TOOL, fakeTool);
            
            final LootContext fakeContext = builder.build(LootParameterSets.BLOCK);
            final LootTable table = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(context.get(LootParameters.BLOCK_STATE).getBlock().getLootTable());
            return table.generate(fakeContext);
        }
        
        return loot;
    }
}
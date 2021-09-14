package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifiers regenerates the loot of a block as if it had silk touch.
 */
public class ModifierSilkTouch extends LootModifier {
    
    public static final GlobalLootModifierSerializer<ModifierSilkTouch> SERIALIZER = new GlobalLootModifierSerializer<ModifierSilkTouch>() {
        
        @Override
        public ModifierSilkTouch read (ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            
            return new ModifierSilkTouch(ailootcondition);
        }
        
        @Override
        public JsonObject write (ModifierSilkTouch instance) {
            
            return new JsonObject();
        }
    };
    
    private ModifierSilkTouch(ILootCondition[] conditionsIn) {
        
        super(conditionsIn);
    }
    
    @Nonnull
    @Override
    public List<ItemStack> doApply (List<ItemStack> loot, LootContext context) {
        
        final ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
            
            final ItemStack fakeTool = tool.copy();
            fakeTool.enchant(Enchantments.SILK_TOUCH, 1);
            
            final LootContext.Builder builder = new LootContext.Builder(context);
            builder.withParameter(LootParameters.TOOL, fakeTool);
            
            final LootContext fakeContext = builder.create(LootParameterSets.BLOCK);
            final LootTable table = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootParameters.BLOCK_STATE).getBlock().getLootTable());
            return table.getRandomItems(fakeContext);
        }
        
        return loot;
    }
}
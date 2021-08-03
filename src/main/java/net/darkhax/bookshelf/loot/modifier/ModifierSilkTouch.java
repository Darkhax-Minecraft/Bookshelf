package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifiers regenerates the loot of a block as if it had silk touch.
 */
public class ModifierSilkTouch extends LootModifier {

    public static final GlobalLootModifierSerializer<ModifierSilkTouch> SERIALIZER = new GlobalLootModifierSerializer<ModifierSilkTouch>() {

        @Override
        public ModifierSilkTouch read (ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {

            return new ModifierSilkTouch(ailootcondition);
        }

        @Override
        public JsonObject write (ModifierSilkTouch instance) {

            return new JsonObject();
        }
    };

    private ModifierSilkTouch (LootItemCondition[] conditionsIn) {

        super(conditionsIn);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply (List<ItemStack> loot, LootContext context) {

        final ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {

            final ItemStack fakeTool = tool.copy();
            fakeTool.enchant(Enchantments.SILK_TOUCH, 1);

            final LootContext.Builder builder = new LootContext.Builder(context);
            builder.withParameter(LootContextParams.TOOL, fakeTool);

            final LootContext fakeContext = builder.create(LootContextParamSets.BLOCK);
            final LootTable table = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
            return table.getRandomItems(fakeContext);
        }

        return loot;
    }
}
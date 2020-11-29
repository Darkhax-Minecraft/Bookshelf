/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.command.ArgumentTypeLootTable;
import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.crafting.block.BlockIngredient;
import net.darkhax.bookshelf.crafting.block.BlockIngredientAny;
import net.darkhax.bookshelf.crafting.block.BlockIngredientCheckBlock;
import net.darkhax.bookshelf.crafting.block.BlockIngredientCheckState;
import net.darkhax.bookshelf.crafting.block.BlockIngredientCheckTag;
import net.darkhax.bookshelf.crafting.item.IngredientEnchantmentType;
import net.darkhax.bookshelf.crafting.item.IngredientModid;
import net.darkhax.bookshelf.crafting.item.IngredientPotion;
import net.darkhax.bookshelf.crafting.item.IngredientToolType;
import net.darkhax.bookshelf.crafting.predicate.ItemPredicateIngredient;
import net.darkhax.bookshelf.crafting.predicate.ItemPredicateModid;
import net.darkhax.bookshelf.crafting.recipes.ShapedRecipeDamaging;
import net.darkhax.bookshelf.crafting.recipes.ShapelessRecipeDamage;
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeEnchantment;
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeFont;
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeRepairCost;
import net.darkhax.bookshelf.internal.command.ArgumentTypeHandOutput;
import net.darkhax.bookshelf.internal.command.BookshelfCommands;
import net.darkhax.bookshelf.loot.condition.CheckBiomeTag;
import net.darkhax.bookshelf.loot.condition.CheckDimensionId;
import net.darkhax.bookshelf.loot.condition.CheckEnchantability;
import net.darkhax.bookshelf.loot.condition.CheckEnergy;
import net.darkhax.bookshelf.loot.condition.CheckHarvestLevel;
import net.darkhax.bookshelf.loot.condition.CheckItem;
import net.darkhax.bookshelf.loot.condition.CheckPower;
import net.darkhax.bookshelf.loot.condition.CheckRaid;
import net.darkhax.bookshelf.loot.condition.CheckRarity;
import net.darkhax.bookshelf.loot.condition.CheckSlimeChunk;
import net.darkhax.bookshelf.loot.condition.CheckStructure;
import net.darkhax.bookshelf.loot.condition.CheckVillage;
import net.darkhax.bookshelf.loot.condition.EntityIsMob;
import net.darkhax.bookshelf.loot.modifier.ModifierAddItem;
import net.darkhax.bookshelf.loot.modifier.ModifierClear;
import net.darkhax.bookshelf.loot.modifier.ModifierConvert;
import net.darkhax.bookshelf.loot.modifier.ModifierRecipe;
import net.darkhax.bookshelf.loot.modifier.ModifierSilkTouch;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bookshelf.MOD_ID)
public final class Bookshelf {
    
    public static Bookshelf instance;
    
    // System Constants
    public static final Random RANDOM = new Random();
    
    public static final String NEW_LINE = System.getProperty("line.separator");
    
    // Mod Constants
    public static final String MOD_ID = "bookshelf";
    
    public static final String MOD_NAME = "Bookshelf";
    
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    
    private final RegistryHelper registry = new RegistryHelper(MOD_ID, LOG);
    
    public final LootConditionType conditionIsMob;
    public final LootConditionType conditionCheckVillage;
    public final LootConditionType conditionCheckStructure;
    public final LootConditionType conditionCheckSlimeChunk;
    public final LootConditionType conditionCheckRarity;
    public final LootConditionType conditionCheckRaid;
    public final LootConditionType conditionCheckPower;
    public final LootConditionType conditionCheckItem;
    public final LootConditionType conditionCheckHarvestLevel;
    public final LootConditionType conditionCheckEnergy;
    public final LootConditionType conditionCheckEnchantability;
    public final LootConditionType conditionCheckBiomeTag;
    public final LootConditionType conditionCheckDimension;
    
    public Bookshelf() {
        
        // Commands
        new BookshelfCommands(this.registry);
        
        // Command arguments
        this.registry.commands.registerCommandArgument("enum", ArgumentTypeHandOutput.class, new ArgumentTypeHandOutput.Serialzier());
        this.registry.commands.registerCommandArgument("mod", ArgumentTypeMod.class, new ArgumentSerializer<>( () -> ArgumentTypeMod.INSTACE));
        this.registry.commands.registerCommandArgument("loot", ArgumentTypeLootTable.class, new ArgumentSerializer<>( () -> ArgumentTypeLootTable.INSTACE));
        
        // Loot Modifier
        this.registry.lootModifiers.register(ModifierClear.SERIALIZER, "clear");
        this.registry.lootModifiers.register(ModifierSilkTouch.SERIALIZER, "silk_touch");
        this.registry.lootModifiers.register(ModifierConvert.SERIALIZER, "convert");
        this.registry.lootModifiers.register(ModifierRecipe.CRAFTING, "crafting");
        this.registry.lootModifiers.register(ModifierRecipe.SMELTING, "smelting");
        this.registry.lootModifiers.register(ModifierRecipe.BLASTING, "blasting");
        this.registry.lootModifiers.register(ModifierRecipe.SMOKING, "smoking");
        this.registry.lootModifiers.register(ModifierRecipe.CAMPFIRE, "campfire_cooking");
        this.registry.lootModifiers.register(ModifierRecipe.STONECUT, "stonecutting");
        this.registry.lootModifiers.register(ModifierRecipe.SMITHING, "smithing");
        this.registry.lootModifiers.register(ModifierAddItem.SERIALIZER, "add_item");
        
        // Loot Conditions
        this.conditionIsMob = this.registry.lootConditions.register(EntityIsMob.SERIALIZER, "is_mob");
        this.conditionCheckVillage = this.registry.lootConditions.register(CheckVillage.SERIALIZER, "check_village");
        this.conditionCheckStructure = this.registry.lootConditions.register(CheckStructure.SERIALIZER, "check_structure");
        this.conditionCheckSlimeChunk = this.registry.lootConditions.register(CheckSlimeChunk.SERIALIZER, "slime_chunk");
        this.conditionCheckRarity = this.registry.lootConditions.register(CheckRarity.SERIALIZER, "check_rarity");
        this.conditionCheckRaid = this.registry.lootConditions.register(CheckRaid.SERIALIZER, "check_raid");
        this.conditionCheckPower = this.registry.lootConditions.register(CheckPower.SERIALIZER, "check_power");
        this.conditionCheckItem = this.registry.lootConditions.register(CheckItem.SERIALIZER, "check_item");
        this.conditionCheckHarvestLevel = this.registry.lootConditions.register(CheckHarvestLevel.SERIALIZER, "check_harvest_level");
        this.conditionCheckEnergy = this.registry.lootConditions.register(CheckEnergy.SERIALIZER, "check_forge_energy");
        this.conditionCheckEnchantability = this.registry.lootConditions.register(CheckEnchantability.SERIALIZER, "check_enchantability");
        this.conditionCheckBiomeTag = this.registry.lootConditions.register(CheckBiomeTag.SERIALIZER, "check_biome_tag");
        this.conditionCheckDimension = this.registry.lootConditions.register(CheckDimensionId.SERIALIZER, "check_dimension");
        
        // Item Predicates
        ItemPredicate.register(new ResourceLocation("bookshelf", "modid"), ItemPredicateModid::fromJson);
        ItemPredicate.register(new ResourceLocation("bookshelf", "ingredient"), ItemPredicateIngredient::fromJson);
        
        // Recipe Serializers
        this.registry.recipeSerializers.register(ShapedRecipeDamaging.SERIALIZER, "crafting_shaped_with_damage");
        this.registry.recipeSerializers.register(ShapelessRecipeDamage.SERIALIZER, "crafting_shapeless_with_damage");
        this.registry.recipeSerializers.register(SmithingRecipeFont.SERIALIZER, "smithing_font");
        this.registry.recipeSerializers.register(SmithingRecipeRepairCost.SERIALIZER, "smithing_repair_cost");
        this.registry.recipeSerializers.register(SmithingRecipeEnchantment.SERIALIZER, "smithing_enchant");
        
        // Ingredients
        this.registry.ingredients.register("potion", IngredientPotion.SERIALIZER);
        this.registry.ingredients.register("modid", IngredientModid.SERIALIZER);
        this.registry.ingredients.register("any_axe", IngredientToolType.create(i -> i instanceof AxeItem, ToolType.AXE));
        this.registry.ingredients.register("any_hoe", IngredientToolType.create(i -> i instanceof HoeItem, ToolType.HOE));
        this.registry.ingredients.register("any_pickaxe", IngredientToolType.create(i -> i instanceof PickaxeItem, ToolType.PICKAXE));
        this.registry.ingredients.register("any_shovel", IngredientToolType.create(i -> i instanceof ShovelItem, ToolType.SHOVEL));
        this.registry.ingredients.register("any_sword", IngredientToolType.create(i -> i instanceof SwordItem, null));
        this.registry.ingredients.register("any_shear", IngredientToolType.create(i -> i instanceof ShearsItem, null));
        
        this.registry.ingredients.register("enchant_armor", IngredientEnchantmentType.create(EnchantmentType.ARMOR));
        this.registry.ingredients.register("enchant_armor_feet", IngredientEnchantmentType.create(EnchantmentType.ARMOR_FEET));
        this.registry.ingredients.register("enchant_armor_legs", IngredientEnchantmentType.create(EnchantmentType.ARMOR_LEGS));
        this.registry.ingredients.register("enchant_armor_chest", IngredientEnchantmentType.create(EnchantmentType.ARMOR_CHEST));
        this.registry.ingredients.register("enchant_armor_head", IngredientEnchantmentType.create(EnchantmentType.ARMOR_HEAD));
        this.registry.ingredients.register("enchant_weapon", IngredientEnchantmentType.create(EnchantmentType.WEAPON));
        this.registry.ingredients.register("enchant_digger", IngredientEnchantmentType.create(EnchantmentType.DIGGER));
        this.registry.ingredients.register("enchant_fishing_rod", IngredientEnchantmentType.create(EnchantmentType.FISHING_ROD));
        this.registry.ingredients.register("enchant_trident", IngredientEnchantmentType.create(EnchantmentType.TRIDENT));
        this.registry.ingredients.register("enchant_breakable", IngredientEnchantmentType.create(EnchantmentType.BREAKABLE));
        this.registry.ingredients.register("enchant_bow", IngredientEnchantmentType.create(EnchantmentType.BOW));
        this.registry.ingredients.register("enchant_wearable", IngredientEnchantmentType.create(EnchantmentType.WEARABLE));
        this.registry.ingredients.register("enchant_crossbow", IngredientEnchantmentType.create(EnchantmentType.CROSSBOW));
        this.registry.ingredients.register("enchant_vanishable", IngredientEnchantmentType.create(EnchantmentType.VANISHABLE));
        
        // Block Ingredients
        BlockIngredient.register(BlockIngredientAny.SERIALIZER, BlockIngredientAny.ID);
        BlockIngredient.register(BlockIngredientCheckState.SERIALIZER, BlockIngredientCheckState.ID);
        BlockIngredient.register(BlockIngredientCheckBlock.SERIALIZER, BlockIngredientCheckBlock.ID);
        BlockIngredient.register(BlockIngredientCheckTag.SERIALIZER, BlockIngredientCheckTag.ID);
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
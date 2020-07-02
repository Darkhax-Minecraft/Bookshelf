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

import net.darkhax.bookshelf.command.ArgumentTypeMod;
import net.darkhax.bookshelf.crafting.item.IngredientModid;
import net.darkhax.bookshelf.crafting.predicate.ItemPredicateIngredient;
import net.darkhax.bookshelf.crafting.predicate.ItemPredicateModid;
import net.darkhax.bookshelf.crafting.recipes.ShapedRecipeDamaging;
import net.darkhax.bookshelf.crafting.recipes.ShapelessRecipeDamage;
import net.darkhax.bookshelf.internal.BookshelfClient;
import net.darkhax.bookshelf.internal.BookshelfIngredients;
import net.darkhax.bookshelf.internal.BookshelfServer;
import net.darkhax.bookshelf.internal.ISidedProxy;
import net.darkhax.bookshelf.internal.command.ArgumentTypeHandOutput;
import net.darkhax.bookshelf.internal.command.BookshelfCommands;
import net.darkhax.bookshelf.loot.condition.CheckBiomeTag;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bookshelf.MOD_ID)
public class Bookshelf {
    
    // System Constants
    public static final Random RANDOM = new Random();
    
    public static final String NEW_LINE = System.getProperty("line.separator");
    
    // Mod Constants
    public static final String MOD_ID = "bookshelf";
    
    public static final String MOD_NAME = "Bookshelf";
    
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    
    public static final ISidedProxy SIDED = DistExecutor.runForDist( () -> () -> new BookshelfClient(), () -> () -> new BookshelfServer());
    
    private final RegistryHelper registry = RegistryHelper.create(MOD_ID, LOG, null);
    
    public Bookshelf() {
        
        // Commands
        new BookshelfCommands(this.registry);
        
        // Command arguments
        this.registry.registerCommandArgument("enum", ArgumentTypeHandOutput.class, new ArgumentTypeHandOutput.Serialzier());
        this.registry.registerCommandArgument("mod", ArgumentTypeMod.class, new ArgumentSerializer<>( () -> ArgumentTypeMod.INSTACE));
        
        // Loot conditions
        this.registry.registerLootCondition(CheckBiomeTag.SERIALIZER);
        this.registry.registerLootCondition(CheckRaid.SERIALIZER);
        this.registry.registerLootCondition(CheckSlimeChunk.SERIALIZER);
        this.registry.registerLootCondition(CheckStructure.SERIALIZER);
        this.registry.registerLootCondition(CheckVillage.SERIALIZER);
        this.registry.registerLootCondition(CheckPower.SERIALIZER);
        this.registry.registerLootCondition(CheckItem.SERIALIZER);
        this.registry.registerLootCondition(CheckHarvestLevel.SERIALIZER);
        this.registry.registerLootCondition(CheckEnchantability.SERIALIZER);
        this.registry.registerLootCondition(CheckRarity.SERIALIZER);
        this.registry.registerLootCondition(CheckEnergy.SERIALIZER);
        this.registry.registerLootCondition(EntityIsMob.SERIALIZER);
        
        // Loot Modifier
        this.registry.registerGlobalModifier(ModifierClear::new, "clear");
        this.registry.registerGlobalModifier(ModifierSilkTouch::new, "silk_touch");
        this.registry.registerGlobalModifier(ModifierConvert.SERIALIZER, "convert");
        this.registry.registerGlobalModifier(ModifierRecipe.CRAFTING, "crafting");
        this.registry.registerGlobalModifier(ModifierRecipe.SMELTING, "smelting");
        this.registry.registerGlobalModifier(ModifierRecipe.BLASTING, "blasting");
        this.registry.registerGlobalModifier(ModifierRecipe.SMOKING, "smoking");
        this.registry.registerGlobalModifier(ModifierRecipe.CAMPFIRE, "campfire_cooking");
        this.registry.registerGlobalModifier(ModifierRecipe.STONECUT, "stonecutting");
        this.registry.registerGlobalModifier(ModifierAddItem.SERIALIZER, "add_item");
        
        // Recipe Types
        this.registry.registerRecipeSerializer(ShapedRecipeDamaging.SERIALIZER, "crafting_shaped_with_damage");
        this.registry.registerRecipeSerializer(ShapelessRecipeDamage.SERIALIZER, "crafting_shapeless_with_damage");
        
        // Ingredient Serializer
        this.registry.registerIngredientType(BookshelfIngredients.ANY_HOE, "any_hoe");
        this.registry.registerIngredientType(BookshelfIngredients.ANY_PICKAXE, "any_pickaxe");
        this.registry.registerIngredientType(BookshelfIngredients.ANY_AXE, "any_axe");
        this.registry.registerIngredientType(BookshelfIngredients.ANY_SHOVEL, "any_shovel");
        this.registry.registerIngredientType(BookshelfIngredients.ANY_SWORD, "any_sword");
        this.registry.registerIngredientType(IngredientModid.SERIALIZER, "modid");
        
        // Item Predicates
        ItemPredicate.register(new ResourceLocation("bookshelf", "modid"), ItemPredicateModid::fromJson);
        ItemPredicate.register(new ResourceLocation("bookshelf", "ingredient"), ItemPredicateIngredient::fromJson);
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
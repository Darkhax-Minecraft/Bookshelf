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
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeEnchantment;
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeFont;
import net.darkhax.bookshelf.crafting.recipes.smithing.SmithingRecipeRepairCost;
import net.darkhax.bookshelf.internal.command.ArgumentTypeHandOutput;
import net.darkhax.bookshelf.internal.command.BookshelfCommands;
import net.darkhax.bookshelf.loot.modifier.ModifierAddItem;
import net.darkhax.bookshelf.loot.modifier.ModifierClear;
import net.darkhax.bookshelf.loot.modifier.ModifierConvert;
import net.darkhax.bookshelf.loot.modifier.ModifierRecipe;
import net.darkhax.bookshelf.loot.modifier.ModifierSilkTouch;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.util.ResourceLocation;
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
    
    private final RegistryHelper registry = new RegistryHelper(MOD_ID, LOG);
    
    public Bookshelf() {
        
        // Commands
        new BookshelfCommands(this.registry);
        
        // Command arguments
        this.registry.commands.registerCommandArgument("enum", ArgumentTypeHandOutput.class, new ArgumentTypeHandOutput.Serialzier());
        this.registry.commands.registerCommandArgument("mod", ArgumentTypeMod.class, new ArgumentSerializer<>( () -> ArgumentTypeMod.INSTACE));
        
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
        this.registry.ingredients.register("modid", IngredientModid.SERIALIZER);
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
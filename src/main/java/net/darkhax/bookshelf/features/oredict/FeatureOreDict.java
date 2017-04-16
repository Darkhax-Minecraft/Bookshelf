/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.features.oredict;

import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.darkhax.bookshelf.features.BookshelfFeature;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@Config(name = "bookshelf")
@BookshelfFeature(name = "oredict", description = "Adds various vanilla things to the oredict")
public class FeatureOreDict extends Feature {
    
    @Configurable(description = "Should the different stone blocks, like andesite being oredict as stone?")
    public static boolean addStone = true;
    
    @Configurable(description = "Should fence gates be oredict?")
    public static boolean addFenceGates = true;
    
    @Configurable(description = "Should beds be oredict?")
    public static boolean addBed = true;
    
    @Configurable(description = "Should trap doors be oredict?")
    public static boolean addTrapdoor = true;
    
    @Override
    public void onPreInit () {
        
        if (addStone) {
            
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 1));
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 2));
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 3));
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 4));
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 5));
            OreDictionary.registerOre(OreDictUtils.STONE, new ItemStack(Blocks.STONE, 1, 6));
        }
        
        if (addFenceGates) {
            
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.OAK_FENCE_GATE);
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.ACACIA_FENCE_GATE);
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.BIRCH_FENCE_GATE);
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.DARK_OAK_FENCE_GATE);
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.JUNGLE_FENCE_GATE);
            OreDictionary.registerOre(OreDictUtils.FENCE_GATE_WOOD, Blocks.SPRUCE_FENCE_GATE);
        }
        
        if (addBed) {
            
            OreDictionary.registerOre(OreDictUtils.BED, Items.BED);
            OreDictionary.registerOre(OreDictUtils.BED_BLOCK, Blocks.BED);
        }
        
        if (addTrapdoor) {
            
            OreDictionary.registerOre(OreDictUtils.TRAPDOOR, Blocks.IRON_TRAPDOOR);
            OreDictionary.registerOre(OreDictUtils.TRAPDOOR, Blocks.TRAPDOOR);
            OreDictionary.registerOre(OreDictUtils.TRAPDOOR_WOOD, Blocks.TRAPDOOR);
        }
    }
}
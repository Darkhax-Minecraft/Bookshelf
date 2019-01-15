/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CollectionUtils {
    
    public static double[] toDouble (float[] input) {
        
        final double[] output = new double[input.length];
        
        for (int index = 0; index < input.length; index++) {
            output[index] = input[index];
        }
        
        return output;
    }
    
    public static float[] toFloat (double[] input) {
        
        final float[] output = new float[input.length];
        
        for (int index = 0; index < input.length; index++) {
            output[index] = (float) input[index];
        }
        
        return output;
    }
    
    public static String[] toString (ResourceLocation[] input) {
        
        final String[] output = new String[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = input[index].toString();
        }
        
        return output;
    }
    
    public static ResourceLocation[] toIdentifier (String[] input) {
        
        final ResourceLocation[] output = new ResourceLocation[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = new ResourceLocation(input[index]);
        }
        
        return output;
    }
    
    public static ResourceLocation[] toIdentifier (Item[] input) {
        
        final ResourceLocation[] output = new ResourceLocation[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = input[index].getRegistryName();
        }
        
        return output;
    }
    
    public static ResourceLocation[] toIdentifier (Block[] input) {
        
        final ResourceLocation[] output = new ResourceLocation[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = input[index].getRegistryName();
        }
        
        return output;
    }
    
    public static Item[] toItem (ResourceLocation[] input) {
        
        final Item[] output = new Item[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = ForgeRegistries.ITEMS.getValue(input[index]);
        }
        
        return output;
    }
    
    public static Block[] toBlock (ResourceLocation[] input) {
        
        final Block[] output = new Block[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = ForgeRegistries.BLOCKS.getValue(input[index]);
        }
        
        return output;
    }
    
    public static String[] toString (ItemStack[] input) {
        
        final String[] output = new String[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = StackUtils.writeStackToString(input[index]);
        }
        
        return output;
    }
    
    public static ItemStack[] toStack (String[] input) {
        
        final ItemStack[] output = new ItemStack[input.length];
        
        for (int index = 0; index < input.length; index++) {
            
            output[index] = StackUtils.createStackFromString(input[index]);
        }
        
        return output;
    }
    
    public static int[] toInt (Color input) {
        
        return new int[] { input.getRed(), input.getGreen(), input.getBlue() };
    }
    
    public static Color toColor (int[] input) {
        
        return input.length == 3 ? new Color(input[0], input[1], input[2]) : Color.BLACK;
    }
    
    public static int[] toInt (BlockPos input) {
        
        return new int[] { input.getX(), input.getY(), input.getZ() };
    }
    
    public static BlockPos toPos (int[] input) {
        
        return input.length == 3 ? new BlockPos(input[0], input[1], input[2]) : BlockPos.ORIGIN;
    }
}
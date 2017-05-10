package net.darkhax.bookshelftest.config;

import java.awt.Color;

import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

@Config(name = "configtest")
public class SecondaryClass {
    
    @Configurable(category = "misc", description = "A configurable boolean")
    public static boolean testBoolean = false;
    
    @Configurable(category = "misc", description = "A configurable boolean array")
    public static boolean[] testBooleanArray = { true, false, false, true };
    
    @Configurable(category = "numbers", description = "A configurable double")
    public static double testDouble = 5.35d;
    
    @Configurable(category = "numbers", description = "A configurable double array")
    public static double[] testDoubleArray = { 1d, 2d, 3.14d };
    
    @Configurable(category = "numbers", description = "A configurable float")
    public static float testFloat = 6.66f;
    
    @Configurable(category = "numbers", description = "A configurable float array")
    public static float[] testFloatArray = { 1f, 2f, 3.14f };
    
    @Configurable(category = "numbers", description = "A configurable int")
    public static int testInt = 69;
    
    @Configurable(category = "numbers", description = "A configurable int array")
    public static int[] testIntArray = { 69, 420, 1337, 9001 };
    
    @Configurable(category = "misc", description = "A configurable string")
    public static String testString = "Test String";
    
    @Configurable(category = "misc", description = "A configurable string array")
    public static String[] testStringArray = { "one", "two", "three" };
    
    @Configurable(category = "custom_objects", description = "A configurable identifier")
    public static ResourceLocation testIdentifier = new ResourceLocation("configtest", "test");
    
    @Configurable(category = "custom_objects", description = "A configurable identifier array")
    public static ResourceLocation[] testIdentifierArray = { new ResourceLocation("configtest", "test1"), new ResourceLocation("configtest", "test2"), new ResourceLocation("configtest", "test3") };
    
    @Configurable(category = "custom_objects", description = "A configurable item")
    public static Item testItem = Items.IRON_INGOT;
    
    @Configurable(category = "custom_objects", description = "A configurable item array")
    public static Item[] testItemArray = { Items.IRON_INGOT, Items.GOLD_INGOT, Items.EMERALD };
    
    @Configurable(category = "custom_objects", description = "A configurable block")
    public static Block testBlock = Blocks.COAL_ORE;
    
    @Configurable(category = "custom_objects", description = "A configurable block array")
    public static Block[] testBlockArray = { Blocks.STONE, Blocks.SAND, Blocks.CLAY };
    
    @Configurable(category = "custom_objects", description = "A configurable item stack")
    public static ItemStack testStack = new ItemStack(Items.DYE, 1, 5);
    
    @Configurable(category = "custom_objects", description = "A configurable item stack array")
    public static ItemStack[] testStackArray = { new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 2) };
    
    @Configurable(category = "custom_objects", description = "A configurable position")
    public static BlockPos testPosition = new BlockPos(-232, 2342, -324231);
    
    @Configurable(category = "custom_objects", description = "A configurable color")
    public static Color testColor = Color.RED;
}

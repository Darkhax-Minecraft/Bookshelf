/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.darkhax.bookshelf.BookshelfConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictUtils {

    // Variant Arrays
    public static final String[] WOOD_TYPES = new String[] { "Oak", "Spruce", "Birch", "Jungle", "Acacia", "DarkOak" };

    // Wooden Stuff
    public static final String LOG_WOOD = "logWood";

    public static final String PLANK_WOOD = "plankWood";

    public static final String SLAB_WOOD = "slabWood";

    public static final String STAIR_WOOD = "stairWood";

    public static final String STICK_WOOD = "stickWood";

    public static final String FENCE_WOOD = "fenceWood";

    public static final String FENCE_GATE_WOOD = "fenceGateWood";

    public static final String DOOR_WOOD = "doorWood";

    public static final String TRAPDOOR_WOOD = "trapdoorWood";

    // Trees
    public static final String TREE_SAPLING = "treeSapling";

    public static final String TREE_LEAVES = "treeLeaves";

    public static final String VINE = "vine";

    // Ores
    public static final String ORE_GOLD = "oreGold";

    public static final String ORE_IRON = "oreIron";

    public static final String ORE_LAPIS = "oreLapis";

    public static final String ORE_DIAMOND = "oreDiamond";

    public static final String ORE_REDSTONE = "oreRedstone";

    public static final String ORE_EMERALD = "oreEmerald";

    public static final String ORE_QUARTZ = "oreQuartz";

    public static final String ORE_COAL = "oreCoal";

    // Metals, Dusts and Gems
    public static final String INGOT_IRON = "ingotIron";

    public static final String INGOT_GOLD = "ingotGold";

    public static final String INGOT_BRICK = "ingotBrick";

    public static final String INGOT_BRICK_NETHER = "ingotBrickNether";

    public static final String NUGGET_GOLD = "nuggetGold";

    public static final String GEM_DIAMOND = "gemDiamond";

    public static final String GEM_EMERALD = "gemEmerald";

    public static final String GEM_QUARTZ = "gemQuartz";

    public static final String GEM_PRISMARINE = "gemPrismarine";

    public static final String DUST_PRISMARINE = "dustPrismarine";

    public static final String DUST_REDSTONE = "dustRedstone";

    public static final String DUST_GLOWSTONE = "dustGlowstone";

    public static final String GEM_LAPIS = "gemLapis";

    public static final String BLOCK_GOLD = "blockGold";

    public static final String BLOCK_IRON = "blockIron";

    public static final String BLOCK_LAPIS = "blockLapis";

    public static final String BLOCK_DIAMOND = "blockDiamond";

    public static final String BLOCK_REDSTONE = "blockRedstone";

    public static final String BLOCK_EMERALD = "blockEmerald";

    public static final String BLOCK_QUARTZ = "blockQuartz";

    public static final String BLOCK_COAL = "blockCoal";

    // Crops
    public static final String CROP_WHEAT = "cropWheat";

    public static final String CROP_POTATO = "cropPotato";

    public static final String CROP_CARROT = "cropCarrot";

    public static final String CROP_NETHER_WART = "cropNetherWart";

    public static final String SUGARCANE = "sugarcane";

    public static final String BLOCK_CACTUS = "blockCactus";

    public static final String CROP_BEETROOT = "cropBeetroot";

    // Drops
    public static final String SLIMEBALL = "slimeball";

    public static final String ENDERPEARL = "enderpearl";

    public static final String BONE = "bone";

    public static final String GUNPOWDER = "gunpowder";

    public static final String STRING = "string";

    public static final String NETHER_STAR = "netherStar";

    public static final String LEATHER = "leather";

    public static final String FEATHER = "feather";

    public static final String EGG = "egg";

    public static final String SHULKER_SHELL = "shulkerShell";

    // Misc Items
    public static final String DYE = "dye";

    public static final String PAPER = "paper";

    public static final String RECORD = "record";

    public static final String BED = "bed";

    public static final String SEED = "seed";

    public static final String ARROW = "arrow";

    // Misc Blocks
    public static final String DIRT = "dirt";

    public static final String GRASS = "grass";

    public static final String STONE = "stone";

    public static final String COBBLESTONE = "cobblestone";

    public static final String GRAVEL = "gravel";

    public static final String SAND = "sand";

    public static final String SANDSTONE = "sandstone";

    public static final String NETHERRACK = "netherrack";

    public static final String OBSIDIAN = "obsidian";

    public static final String GLOWSTONE = "glowstone";

    public static final String ENDSTONE = "endstone";

    public static final String TORCH = "torch";

    public static final String WORKBENCH = "workbench";

    public static final String BLOCK_SLIME = "blockSlime";

    public static final String BLOCK_PRISMARINE = "blockPrismarine";

    public static final String BLOCK_PRISMARINE_BRICK = "blockPrismarineBrick";

    public static final String BLOCK_PRISMARINE_DARK = "blockPrismarineDark";

    public static final String STONE_GRANITE = "stoneGranite";

    public static final String STONE_GRANITE_POLISHED = "stoneGranitePolished";

    public static final String STONE_DIORITE = "stoneDiorite";

    public static final String STONE_DIORITE_POLISHED = "stoneDioritePolished";

    public static final String STONE_ANDESITE = "stoneAndesite";

    public static final String STONE_ANDESITE_POLISHED = "stoneAndesitePolished";

    public static final String BLOCK_GLASS_COLORLESS = "blockGlassColorless";

    public static final String BLOCK_GLASS = "blockGlass";

    public static final String PANE_GLASS_COLORLESS = "paneGlassColorless";

    public static final String PANE_GLASS = "paneGlass";

    public static final String CHEST = "chest";

    public static final String CHEST_WOOD = "chestWood";

    public static final String CHEST_ENDER = "chestEnder";

    public static final String CHEST_TRAPPED = "chestTrapped";

    public static final String TRAPDOOR = "trapdoor";

    public static final String HOPPER = "hopper";

    public static final String BOOKSHELF = "bookshelf";

    public static final String SHULKER_BOX = "shulkerBox";

    public static final String RAIL = "rail";

    public static final String BLOCK_MAGMA = "blockMagma";

    public static final String BLOCK_NETHER_WART = "blockNetherWart";

    public static final String BLOCK_BONE = "blockBone";

    // Black
    public static final String DYE_BLACK = "dyeBlack";

    public static final String BLOCK_GLASS_BLACK = "blockGlassBlack";

    public static final String PANE_GLASS_BLACK = "paneGlassBlack";

    // Red
    public static final String DYE_RED = "dyeRed";

    public static final String BLOCK_GLASS_RED = "blockGlassRed";

    public static final String PANE_GLASS_RED = "paneGlassRed";

    // Green
    public static final String DYE_GREEN = "dyeGreen";

    public static final String BLOCK_GLASS_GREEN = "blockGlassGreen";

    public static final String PANE_GLASS_GREEN = "paneGlassGreen";

    // Brown
    public static final String DYE_BROWN = "dyeBrown";

    public static final String BLOCK_GLASS_BROWN = "blockGlassBrown";

    public static final String PANE_GLASS_BROWN = "paneGlassBrown";

    // Blue
    public static final String DYE_BLUE = "dyeBlue";

    public static final String BLOCK_GLASS_BLUE = "blockGlassBlue";

    public static final String PANE_GLASS_BLUE = "paneGlassBlue";

    // Purple
    public static final String DYE_PURPLE = "dyePurple";

    public static final String BLOCK_GLASS_PURPLE = "blockGlassPurple";

    public static final String PANE_GLASS_PURPLE = "paneGlassPurple";

    // Cyan
    public static final String DYE_CYAN = "dyeCyan";

    public static final String BLOCK_GLASS_CYAN = "blockGlassCyan";

    public static final String PANE_GLASS_CYAN = "paneGlassCyan";

    // Light Gray
    public static final String DYE_LIGHT_GRAY = "dyeLightGray";

    public static final String BLOCK_GLASS_LIGHT_GRAY = "blockGlassLightGray";

    public static final String PANE_GLASS_LIGHT_GRAY = "paneGlassLightGray";

    // Gray
    public static final String DYE_GRAY = "dyeGray";

    public static final String BLOCK_GLASS_GRAY = "blockGlassGray";

    public static final String PANE_GLASS_GRAY = "paneGlassGray";

    // Pink
    public static final String DYE_PINK = "dyePink";

    public static final String BLOCK_GLASS_PINK = "blockGlassPink";

    public static final String PANE_GLASS_PINK = "paneGlassPink";

    // Lime
    public static final String DYE_LIME = "dyeLime";

    public static final String BLOCK_GLASS_LIME = "blockGlassLime";

    public static final String PANE_GLASS_LIME = "paneGlassLime";

    // Yellow
    public static final String DYE_YELLOW = "dyeYellow";

    public static final String BLOCK_GLASS_YELLOW = "blockGlassYellow";

    public static final String PANE_GLASS_YELLOW = "paneGlassYellow";

    // Blue
    public static final String DYE_LIGHT_BLUE = "dyeLightBlue";

    public static final String BLOCK_GLASS_LIGHT_BLUE = "blockGlassLightBlue";

    public static final String PANE_GLASS_LIGHT_BLUE = "paneGlassLightBlue";

    // Magenta
    public static final String DYE_MAGENTA = "dyeMagenta";

    public static final String BLOCK_GLASS_MAGENTA = "blockGlassMagenta";

    public static final String PANE_GLASS_MAGENTA = "paneGlassMagenta";

    // Orange
    public static final String DYE_ORANGE = "dyeOrange";

    public static final String BLOCK_GLASS_ORANGE = "blockGlassOrange";

    public static final String PANE_GLASS_ORANGE = "paneGlassOrange";

    // White
    public static final String DYE_WHITE = "dyeWhite";

    public static final String BLOCK_GLASS_WHITE = "blockGlassWhite";

    public static final String PANE_GLASS_WHITE = "paneGlassWhite";

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private OreDictUtils () {

        throw new IllegalAccessError("Utility class");
    }

    public static void initAdditionalVanillaEntries () {

        if (BookshelfConfig.oreDictMisc) {

            OreDictionary.registerOre(BED, Items.BED);

            OreDictionary.registerOre(HOPPER, Blocks.HOPPER);

            OreDictionary.registerOre(BOOKSHELF, Blocks.BOOKSHELF);

            OreDictionary.registerOre(CROP_BEETROOT, Items.BEETROOT);

            OreDictionary.registerOre(BLOCK_MAGMA, Blocks.MAGMA);

            OreDictionary.registerOre(BLOCK_NETHER_WART, Blocks.NETHER_WART_BLOCK);

            OreDictionary.registerOre(BLOCK_BONE, Blocks.BONE_BLOCK);

            OreDictionary.registerOre(SHULKER_SHELL, Items.SHULKER_SHELL);
        }

        if (BookshelfConfig.oreDictFence) {

            OreDictionary.registerOre(FENCE_WOOD, Blocks.OAK_FENCE);
            OreDictionary.registerOre(FENCE_WOOD, Blocks.SPRUCE_FENCE);
            OreDictionary.registerOre(FENCE_WOOD, Blocks.BIRCH_FENCE);
            OreDictionary.registerOre(FENCE_WOOD, Blocks.JUNGLE_FENCE);
            OreDictionary.registerOre(FENCE_WOOD, Blocks.ACACIA_FENCE);
            OreDictionary.registerOre(FENCE_WOOD, Blocks.DARK_OAK_FENCE);

            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.OAK_FENCE_GATE);
            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.SPRUCE_FENCE_GATE);
            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.BIRCH_FENCE_GATE);
            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.JUNGLE_FENCE_GATE);
            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.ACACIA_FENCE_GATE);
            OreDictionary.registerOre(FENCE_GATE_WOOD, Blocks.DARK_OAK_FENCE_GATE);
        }

        if (BookshelfConfig.oreDictShulker) {

            OreDictionary.registerOre(SHULKER_BOX, Blocks.WHITE_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.LIME_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.PINK_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.GRAY_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.SILVER_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.CYAN_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.BLUE_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.BROWN_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.GREEN_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.RED_SHULKER_BOX);
            OreDictionary.registerOre(SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);

            OreDictionary.registerOre(CHEST, Blocks.WHITE_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.ORANGE_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.MAGENTA_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.LIGHT_BLUE_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.YELLOW_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.LIME_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.PINK_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.GRAY_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.SILVER_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.CYAN_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.PURPLE_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.BLUE_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.BROWN_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.GREEN_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.RED_SHULKER_BOX);
            OreDictionary.registerOre(CHEST, Blocks.BLACK_SHULKER_BOX);
        }

        if (BookshelfConfig.oreDictSeeds) {

            OreDictionary.registerOre(SEED, Items.WHEAT_SEEDS);
            OreDictionary.registerOre(SEED, Items.PUMPKIN_SEEDS);
            OreDictionary.registerOre(SEED, Items.MELON_SEEDS);
            OreDictionary.registerOre(SEED, Items.BEETROOT_SEEDS);
        }

        if (BookshelfConfig.oreDictRails) {

            OreDictionary.registerOre(RAIL, Blocks.RAIL);
            OreDictionary.registerOre(RAIL, Blocks.ACTIVATOR_RAIL);
            OreDictionary.registerOre(RAIL, Blocks.DETECTOR_RAIL);
            OreDictionary.registerOre(RAIL, Blocks.GOLDEN_RAIL);
        }

        if (BookshelfConfig.oreDictArrows) {

            OreDictionary.registerOre(ARROW, Items.ARROW);
            OreDictionary.registerOre(ARROW, Items.SPECTRAL_ARROW);
            OreDictionary.registerOre(ARROW, Items.TIPPED_ARROW);
        }
    }
}
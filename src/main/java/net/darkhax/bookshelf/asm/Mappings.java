package net.darkhax.bookshelf.asm;

public class Mappings {
    
    // Fields
    public static String rand;
    public static String enchantLevels;
    public static String dataWatcher;
    public static String stick;
    public static String armorValues;
    public static String isHorseSaddled;
    public static String horseTextureArray;
    public static String texturePrefix;
    public static String iron_horse_armor;
    public static String diamond_horse_armor;
    public static String horseChest;
    
    // Methods
    public static String buildEnchantmentList;
    public static String enchantItem;
    public static String interact;
    public static String isArmorItem;
    public static String entityInit;
    public static String updateHorseSlots;
    public static String onInventoryChanged;
    public static String getTotalArmorValue;
    public static String setHorseTexturePaths;
    public static String getWatchableObjectItemStack;
    public static String updateObject;
    public static String getHorseArmorIndexSynced;
    public static String getItem;
    public static String playSound;
    public static String isItemEqual;
    public static String addObject;
    public static String getStackInSlot;
    public static String getUnlocalizedName;
    public static String getInteger;
    public static String hasKey;
    public static String getTagCompound;
    public static String hasTagCompound;
    public static String getColorFromItemStack;
    public static String updateSpawner;
    public static String updatePistonState;
    public static String canPushBlock;
    public static String getLootingModifier;
    public static String displayAllReleventItems;
    public static String getRelevantEnchantmentTypes;
    public static String addEnchantmentBooksToList;
    
    public Mappings() {
        
        // Fields
        rand = ASMHelper.getAppropriateMapping("rand", "field_75169_l");
        enchantLevels = ASMHelper.getAppropriateMapping("enchantLevels", "field_75167_g");
        dataWatcher = ASMHelper.getAppropriateMapping("dataWatcher", "field_70180_af");
        stick = ASMHelper.getAppropriateMapping("stick", "field_151055_y");
        armorValues = ASMHelper.getAppropriateMapping("armorValues", "field_110272_by");
        isHorseSaddled = ASMHelper.getAppropriateMapping("isHorseSaddled", "func_110257_ck");
        horseTextureArray = ASMHelper.getAppropriateMapping("field_110280_bR", "field_110280_bR");
        texturePrefix = ASMHelper.getAppropriateMapping("field_110286_bQ", "field_110286_bQ");
        iron_horse_armor = ASMHelper.getAppropriateMapping("iron_horse_armor", "field_151138_bX");
        diamond_horse_armor = ASMHelper.getAppropriateMapping("diamond_horse_armor", "field_151125_bZ");
        horseChest = ASMHelper.getAppropriateMapping("horseChest", "field_110296_bG");
        
        // Methods
        buildEnchantmentList = ASMHelper.getAppropriateMapping("buildEnchantmentList", "func_77513_b");
        enchantItem = ASMHelper.getAppropriateMapping("enchantItem", "func_75140_a");
        interact = ASMHelper.getAppropriateMapping("interact", "func_70085_c");
        isArmorItem = ASMHelper.getAppropriateMapping("func_146085_a", "func_146085_a");
        entityInit = ASMHelper.getAppropriateMapping("entityInit", "func_70088_a");
        updateHorseSlots = ASMHelper.getAppropriateMapping("func_110232_cE", "func_110232_cE");
        onInventoryChanged = ASMHelper.getAppropriateMapping("onInventoryChanged", "func_76316_a");
        getTotalArmorValue = ASMHelper.getAppropriateMapping("getTotalArmorValue", "func_70658_aO");
        setHorseTexturePaths = ASMHelper.getAppropriateMapping("setHorseTexturePaths", "func_110247_cG");
        getWatchableObjectItemStack = ASMHelper.getAppropriateMapping("getWatchableObjectItemStack", "func_82710_f");
        updateObject = ASMHelper.getAppropriateMapping("updateObject", "func_75692_b");
        getHorseArmorIndexSynced = ASMHelper.getAppropriateMapping("func_110241_cb", "func_110241_cb");
        getItem = ASMHelper.getAppropriateMapping("getItem", "func_77973_b");
        playSound = ASMHelper.getAppropriateMapping("playSound", "func_85030_a");
        isItemEqual = ASMHelper.getAppropriateMapping("isItemEqual", "func_77969_a");
        addObject = ASMHelper.getAppropriateMapping("addObject", "func_75682_a");
        getStackInSlot = ASMHelper.getAppropriateMapping("getStackInSlot", "func_70301_a");
        getUnlocalizedName = ASMHelper.getAppropriateMapping("getUnlocalizedName", "func_77977_a");
        getInteger = ASMHelper.getAppropriateMapping("getInteger", "func_74762_e");
        hasKey = ASMHelper.getAppropriateMapping("hasKey", "func_74764_b");
        getTagCompound = ASMHelper.getAppropriateMapping("getTagCompound", "func_77978_p");
        hasTagCompound = ASMHelper.getAppropriateMapping("hasTagCompound", "func_77942_o");
        getColorFromItemStack = ASMHelper.getAppropriateMapping("getColorFromItemStack", "func_82790_a");
        updateSpawner = ASMHelper.getAppropriateMapping("updateSpawner", "func_98278_g");
        updatePistonState = ASMHelper.getAppropriateMapping("updatePistonState", "func_150078_e");
        canPushBlock = ASMHelper.getAppropriateMapping("canPushBlock", "");
        getLootingModifier = ASMHelper.getAppropriateMapping("getLootingModifier", "func_77519_f");
        displayAllReleventItems = ASMHelper.getAppropriateMapping("displayAllReleventItems", "func_78018_a");
        getRelevantEnchantmentTypes = ASMHelper.getAppropriateMapping("func_111225_m", "func_111225_m");
        addEnchantmentBooksToList = ASMHelper.getAppropriateMapping("addEnchantmentBooksToList", "func_92116_a");
    }
}
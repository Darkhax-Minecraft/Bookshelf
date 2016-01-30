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
    public static String potionID;
    
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
    public static String getKnockbackModifier;
    public static String getFireAspectModifier;
    public static String getRespiration;
    public static String getDepthStriderModifier;
    public static String getEfficiencyModifier;
    public static String getFortuneModifier;
    public static String getLuckOfSeaModifier;
    public static String getLureModifier;
    public static String getLootingModifier;
    public static String displayAllReleventItems;
    public static String getRelevantEnchantmentTypes;
    public static String addEnchantmentBooksToList;
    public static String onNewPotionEffect;
    public static String onChangedPotionEffect;
    public static String onFinishedPotionEffect;
    
    public static void initFields () {
        
        rand = ASMUtils.getAppropriateMapping("rand", "field_75169_l");
        enchantLevels = ASMUtils.getAppropriateMapping("enchantLevels", "field_75167_g");
        dataWatcher = ASMUtils.getAppropriateMapping("dataWatcher", "field_70180_af");
        stick = ASMUtils.getAppropriateMapping("stick", "field_151055_y");
        armorValues = ASMUtils.getAppropriateMapping("armorValues", "field_110272_by");
        isHorseSaddled = ASMUtils.getAppropriateMapping("isHorseSaddled", "func_110257_ck");
        horseTextureArray = ASMUtils.getAppropriateMapping("field_110280_bR", "field_110280_bR");
        texturePrefix = ASMUtils.getAppropriateMapping("field_110286_bQ", "field_110286_bQ");
        iron_horse_armor = ASMUtils.getAppropriateMapping("iron_horse_armor", "field_151138_bX");
        diamond_horse_armor = ASMUtils.getAppropriateMapping("diamond_horse_armor", "field_151125_bZ");
        horseChest = ASMUtils.getAppropriateMapping("horseChest", "field_110296_bG");
        potionID = ASMUtils.getAppropriateMapping("id", "field_76415_H");
    }
    
    public static void initMethods () {
        
        buildEnchantmentList = ASMUtils.getAppropriateMapping("buildEnchantmentList", "func_77513_b");
        enchantItem = ASMUtils.getAppropriateMapping("enchantItem", "func_75140_a");
        interact = ASMUtils.getAppropriateMapping("interact", "func_70085_c");
        isArmorItem = ASMUtils.getAppropriateMapping("func_146085_a", "func_146085_a");
        entityInit = ASMUtils.getAppropriateMapping("entityInit", "func_70088_a");
        updateHorseSlots = ASMUtils.getAppropriateMapping("func_110232_cE", "func_110232_cE");
        onInventoryChanged = ASMUtils.getAppropriateMapping("onInventoryChanged", "func_76316_a");
        getTotalArmorValue = ASMUtils.getAppropriateMapping("getTotalArmorValue", "func_70658_aO");
        setHorseTexturePaths = ASMUtils.getAppropriateMapping("setHorseTexturePaths", "func_110247_cG");
        getWatchableObjectItemStack = ASMUtils.getAppropriateMapping("getWatchableObjectItemStack", "func_82710_f");
        updateObject = ASMUtils.getAppropriateMapping("updateObject", "func_75692_b");
        getHorseArmorIndexSynced = ASMUtils.getAppropriateMapping("func_110241_cb", "func_110241_cb");
        getItem = ASMUtils.getAppropriateMapping("getItem", "func_77973_b");
        playSound = ASMUtils.getAppropriateMapping("playSound", "func_85030_a");
        isItemEqual = ASMUtils.getAppropriateMapping("isItemEqual", "func_77969_a");
        addObject = ASMUtils.getAppropriateMapping("addObject", "func_75682_a");
        getStackInSlot = ASMUtils.getAppropriateMapping("getStackInSlot", "func_70301_a");
        getUnlocalizedName = ASMUtils.getAppropriateMapping("getUnlocalizedName", "func_77977_a");
        getInteger = ASMUtils.getAppropriateMapping("getInteger", "func_74762_e");
        hasKey = ASMUtils.getAppropriateMapping("hasKey", "func_74764_b");
        getTagCompound = ASMUtils.getAppropriateMapping("getTagCompound", "func_77978_p");
        hasTagCompound = ASMUtils.getAppropriateMapping("hasTagCompound", "func_77942_o");
        getColorFromItemStack = ASMUtils.getAppropriateMapping("getColorFromItemStack", "func_82790_a");
        updateSpawner = ASMUtils.getAppropriateMapping("updateSpawner", "func_98278_g");
        updatePistonState = ASMUtils.getAppropriateMapping("updatePistonState", "func_150078_e");
        canPushBlock = ASMUtils.getAppropriateMapping("canPushBlock", "");
        
        getKnockbackModifier = ASMUtils.getAppropriateMapping("getKnockbackModifier", "func_77501_a");
        getFireAspectModifier = ASMUtils.getAppropriateMapping("getFireAspectModifier", "func_90036_a");
        
        // (Lnet/minecraft/entity/Entity;)I
        getRespiration = ASMUtils.getAppropriateMapping("getRespiration", "func_180319_a");
        getDepthStriderModifier = ASMUtils.getAppropriateMapping("getDepthStriderModifier", "func_180318_b");
        
        getEfficiencyModifier = ASMUtils.getAppropriateMapping("getEfficiencyModifier", "func_77509_b");
        getFortuneModifier = ASMUtils.getAppropriateMapping("getFortuneModifier", "func_77517_e");
        getLuckOfSeaModifier = ASMUtils.getAppropriateMapping("getLuckOfSeaModifier", "func_151386_g");
        getLureModifier = ASMUtils.getAppropriateMapping("getLureModifier", "func_151387_h ");
        getLootingModifier = ASMUtils.getAppropriateMapping("getLootingModifier", "func_77519_f");
        
        displayAllReleventItems = ASMUtils.getAppropriateMapping("displayAllReleventItems", "func_78018_a");
        getRelevantEnchantmentTypes = ASMUtils.getAppropriateMapping("func_111225_m", "func_111225_m");
        addEnchantmentBooksToList = ASMUtils.getAppropriateMapping("addEnchantmentBooksToList", "func_92116_a");
        onNewPotionEffect = ASMUtils.getAppropriateMapping("onNewPotionEffect", "func_70670_a");
        onChangedPotionEffect = ASMUtils.getAppropriateMapping("onChangedPotionEffect", "func_70695_b");
        onFinishedPotionEffect = ASMUtils.getAppropriateMapping("onFinishedPotionEffect", "func_70688_c");
    }
}

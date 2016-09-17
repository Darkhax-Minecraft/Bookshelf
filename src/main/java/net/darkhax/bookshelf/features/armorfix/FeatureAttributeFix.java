package net.darkhax.bookshelf.features.armorfix;

import net.darkhax.bookshelf.features.Feature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.common.config.Configuration;

public class FeatureAttributeFix extends Feature {
    
    private final String CONFIG_NAME = "AttributeFix";
    private boolean enabled = true;
    private float maxArmor = 1024f;
    private float maxArmorToughness = 1024f;
    private float maxHealth = 4096f;
    
    @Override
    public void onPreInit () {
        
        if (this.enabled) {
            
            SharedMonsterAttributes.MAX_HEALTH = (new RangedAttribute((IAttribute)null, "generic.maxHealth", 20.0D, 0.0D, this.maxHealth)).setDescription("Max Health").setShouldWatch(true);
            SharedMonsterAttributes.ARMOR = (new RangedAttribute((IAttribute)null, "generic.armor", 0.0D, 0.0D, this.maxArmor)).setShouldWatch(true);
            SharedMonsterAttributes.ARMOR_TOUGHNESS = (new RangedAttribute((IAttribute)null, "generic.armorToughness", 0.0D, 0.0D, this.maxArmorToughness)).setShouldWatch(true);
        }
    }
    
    @Override
    public void setupConfig (Configuration config) {
        
        this.enabled = config.getBoolean("Enabled", CONFIG_NAME, true, "Should the max armor amount cap be raised?");
        this.maxArmor = config.getFloat("MaxArmor", CONFIG_NAME, 1024f, 0f, Float.MAX_VALUE, "The highest possible amount of armor points. Vanilla caps this at 30!");
        this.maxArmorToughness = config.getFloat("MaxArmorToughness", CONFIG_NAME, 1024f, 0f, Float.MAX_VALUE, "The highest possible amount of armor points. Vanilla caps this at 20!");
        this.maxArmor = config.getFloat("MaxHealth", CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible amount of health. Vanilla caps this at 1024!");
    }
}
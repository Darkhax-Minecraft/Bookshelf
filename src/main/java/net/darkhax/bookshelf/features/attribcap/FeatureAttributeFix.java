package net.darkhax.bookshelf.features.attribcap;

import net.darkhax.bookshelf.features.Feature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.common.config.Configuration;

public class FeatureAttributeFix extends Feature {

    private final String CONFIG_NAME = "AttributeFix";

    private boolean enabled = true;

    private final float maxHealth = 4096f;

    private float followRange = 4096f;

    private float knockback = 4096f;

    private float speed = 4096f;

    private float damage = 4096f;

    private float atkSpeed = 4096f;

    private float maxArmor = 4096f;

    private float maxArmorToughness = 4096f;

    private float luck = 4096f;

    @Override
    public void onPreInit () {

        if (this.enabled) {

            SharedMonsterAttributes.MAX_HEALTH = new RangedAttribute((IAttribute) null, "generic.maxHealth", 20.0D, 0.0D, this.maxHealth).setDescription("Max Health").setShouldWatch(true);
            SharedMonsterAttributes.FOLLOW_RANGE = new RangedAttribute((IAttribute) null, "generic.followRange", 32.0D, 0.0D, this.followRange).setDescription("Follow Range");
            SharedMonsterAttributes.KNOCKBACK_RESISTANCE = new RangedAttribute((IAttribute) null, "generic.knockbackResistance", 0.0D, 0.0D, this.knockback).setDescription("Knockback Resistance");
            SharedMonsterAttributes.MOVEMENT_SPEED = new RangedAttribute((IAttribute) null, "generic.movementSpeed", 0.699999988079071D, 0.0D, this.speed).setDescription("Movement Speed").setShouldWatch(true);
            SharedMonsterAttributes.ATTACK_DAMAGE = new RangedAttribute((IAttribute) null, "generic.attackDamage", 2.0D, 0.0D, this.damage);
            SharedMonsterAttributes.ATTACK_SPEED = new RangedAttribute((IAttribute) null, "generic.attackSpeed", 4.0D, 0.0D, this.atkSpeed).setShouldWatch(true);
            SharedMonsterAttributes.ARMOR = new RangedAttribute((IAttribute) null, "generic.armor", 0.0D, 0.0D, this.maxArmor).setShouldWatch(true);
            SharedMonsterAttributes.ARMOR_TOUGHNESS = new RangedAttribute((IAttribute) null, "generic.armorToughness", 0.0D, 0.0D, this.maxArmorToughness).setShouldWatch(true);
            SharedMonsterAttributes.LUCK = new RangedAttribute((IAttribute) null, "generic.luck", 0.0D, -1024.0D, this.luck).setShouldWatch(true);
        }
    }

    @Override
    public void setupConfig (Configuration config) {

        this.enabled = config.getBoolean("Enabled", this.CONFIG_NAME, true, "Should the max armor amount cap be raised?");

        this.maxArmor = config.getFloat("MaxHealth", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible amount of health.");
        this.followRange = config.getFloat("FollowRange", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible follow range.");
        this.knockback = config.getFloat("Knockback", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible knockback resistance");
        this.speed = config.getFloat("Speed", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible movement speed.");
        this.damage = config.getFloat("Damage", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible attack damage");
        this.atkSpeed = config.getFloat("AttackSpeed", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible attack speed");
        this.maxArmor = config.getFloat("MaxArmor", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible amount of armor points.");
        this.maxArmorToughness = config.getFloat("MaxArmorToughness", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible amount of armor toughness.");
        this.luck = config.getFloat("Luck", this.CONFIG_NAME, 4096f, 0f, Float.MAX_VALUE, "The highest possible amount of luck.");
    }
}
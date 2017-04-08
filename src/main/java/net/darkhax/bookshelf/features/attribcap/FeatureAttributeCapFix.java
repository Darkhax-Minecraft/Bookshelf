package net.darkhax.bookshelf.features.attribcap;

import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.darkhax.bookshelf.features.BookshelfFeature;
import net.darkhax.bookshelf.features.Feature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

@Config(name = "bookshelf")
@BookshelfFeature(name = "attributefix", description = "Allows for the cap of certain game values to be modified from the vanilla caps")
public class FeatureAttributeCapFix extends Feature {

    @Configurable(category = "attributefix", description = "The highest possible max health.")
    public static float maxHealth = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible follow range.")
    public static float followRange = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible knockback")
    public static float knockback = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible speed")
    public static float speed = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible attack damage")
    public static float damage = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible attack speed")
    public static float atkSpeed = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible armor value")
    public static float maxArmor = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible armor toughness")
    public static float maxArmorToughness = 4096f;

    @Configurable(category = "attributefix", description = "The highest possible luck value")
    public static float luck = 4096f;

    @Override
    public void onPreInit () {

        SharedMonsterAttributes.MAX_HEALTH = new RangedAttribute((IAttribute) null, "generic.maxHealth", 20.0D, 0.0D, maxHealth).setDescription("Max Health").setShouldWatch(true);
        SharedMonsterAttributes.FOLLOW_RANGE = new RangedAttribute((IAttribute) null, "generic.followRange", 32.0D, 0.0D, followRange).setDescription("Follow Range");
        SharedMonsterAttributes.KNOCKBACK_RESISTANCE = new RangedAttribute((IAttribute) null, "generic.knockbackResistance", 0.0D, 0.0D, knockback).setDescription("Knockback Resistance");
        SharedMonsterAttributes.MOVEMENT_SPEED = new RangedAttribute((IAttribute) null, "generic.movementSpeed", 0.699999988079071D, 0.0D, speed).setDescription("Movement Speed").setShouldWatch(true);
        SharedMonsterAttributes.ATTACK_DAMAGE = new RangedAttribute((IAttribute) null, "generic.attackDamage", 2.0D, 0.0D, damage);
        SharedMonsterAttributes.ATTACK_SPEED = new RangedAttribute((IAttribute) null, "generic.attackSpeed", 4.0D, 0.0D, atkSpeed).setShouldWatch(true);
        SharedMonsterAttributes.ARMOR = new RangedAttribute((IAttribute) null, "generic.armor", 0.0D, 0.0D, maxArmor).setShouldWatch(true);
        SharedMonsterAttributes.ARMOR_TOUGHNESS = new RangedAttribute((IAttribute) null, "generic.armorToughness", 0.0D, 0.0D, maxArmorToughness).setShouldWatch(true);
        SharedMonsterAttributes.LUCK = new RangedAttribute((IAttribute) null, "generic.luck", 0.0D, -1024.0D, luck).setShouldWatch(true);
    }
}
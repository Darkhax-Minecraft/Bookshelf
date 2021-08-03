/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class EnchantmentAttribute extends EnchantmentTicking {

    private final Map<Attribute, AttributeModifier> modifiers = new HashMap<>();

    public EnchantmentAttribute (Rarity rarity, EnchantmentCategory type, EquipmentSlot... slots) {

        super(rarity, type, slots);
    }

    public EnchantmentAttribute addAttributeModifier (Attribute attribute, AttributeModifier modifier) {

        this.modifiers.put(attribute, modifier);
        return this;
    }

    public Map<Attribute, AttributeModifier> getModifiers (int level) {

        return this.modifiers;
    }

    protected void removeModifiers (LivingEntity living, int level) {

        final AttributeMap attributeMap = living.getAttributes();

        for (final Entry<Attribute, AttributeModifier> entry : this.getModifiers(level).entrySet()) {

            final AttributeInstance modifiable = attributeMap.getInstance(entry.getKey());

            if (modifiable != null) {

                modifiable.removeModifier(entry.getValue());
            }
        }
    }

    protected void applyModifiers (LivingEntity living, int level) {

        final AttributeMap attributeMap = living.getAttributes();

        for (final Entry<Attribute, AttributeModifier> entry : this.getModifiers(level).entrySet()) {

            final AttributeInstance modifiable = attributeMap.getInstance(entry.getKey());

            if (modifiable != null) {

                final AttributeModifier effectModifier = entry.getValue();
                modifiable.removeModifier(effectModifier);
                modifiable.addPermanentModifier(effectModifier);
            }
        }
    }

    @Override
    public void onUserTick (LivingEntity user, int level) {

        this.removeModifiers(user, level);

        if (level > 0) {

            this.applyModifiers(user, level);
        }
    }
}
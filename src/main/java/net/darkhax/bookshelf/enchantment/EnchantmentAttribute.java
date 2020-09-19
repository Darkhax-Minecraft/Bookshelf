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

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentAttribute extends EnchantmentTicking {
    
    private final Map<Attribute, AttributeModifier> modifiers = new HashMap<>();
    
    public EnchantmentAttribute(Rarity rarity, EnchantmentType type, EquipmentSlotType... slots) {
        
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
        
        final AttributeModifierManager attributeMap = living.getAttributeManager();
        
        for (final Entry<Attribute, AttributeModifier> entry : this.getModifiers(level).entrySet()) {
            
            final ModifiableAttributeInstance modifiable = attributeMap.createInstanceIfAbsent(entry.getKey());
            
            if (modifiable != null) {
                
                modifiable.removeModifier(entry.getValue());
            }
        }
    }
    
    protected void applyModifiers (LivingEntity living, int level) {
        
        final AttributeModifierManager attributeMap = living.getAttributeManager();
        
        for (final Entry<Attribute, AttributeModifier> entry : this.getModifiers(level).entrySet()) {
            
            final ModifiableAttributeInstance modifiable = attributeMap.createInstanceIfAbsent(entry.getKey());
            
            if (modifiable != null) {
                
                final AttributeModifier effectModifier = entry.getValue();
                modifiable.removeModifier(effectModifier);
                modifiable.applyPersistentModifier(effectModifier);
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
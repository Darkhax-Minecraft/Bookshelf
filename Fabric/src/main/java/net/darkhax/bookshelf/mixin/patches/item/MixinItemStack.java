package net.darkhax.bookshelf.mixin.patches.item;

import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.impl.event.FabricBookshelfEvents;
import net.darkhax.bookshelf.impl.event.FabricItemAttributeEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ItemStack.class)
public class MixinItemStack {

    /**
     * Introduces an event that allows listeners to modify item stack attributes.
     */
    @ModifyVariable(method = "getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public Multimap<Attribute, AttributeModifier> crafttweaker$getAttributeModifiers$modifyAttributes(Multimap<Attribute, AttributeModifier> multimap, EquipmentSlot slot) {
        final FabricItemAttributeEvent event = new FabricItemAttributeEvent((ItemStack) (Object) this, slot, multimap);
        FabricBookshelfEvents.ITEM_ATTRIBUTE_EVENT.invoker().accept(event);
        return event.getModifiers();
    }
}
package net.darkhax.bookshelf.handler;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.common.network.packet.PacketBuffUpdate;
import net.darkhax.bookshelf.event.CreativeTabEvent;
import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.SkullUtils;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.darkhax.bookshelf.potion.BuffEffect;
import net.darkhax.bookshelf.potion.BuffHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ForgeEventHandler {
    
    @SubscribeEvent
    public void post (CreativeTabEvent.Post event) {
        
        if (event.tab == CreativeTabs.tabDecorations)
            for (ItemStack stack : SkullUtils.getMHFSkulls())
                event.itemList.add(stack);
    }
    
    @SubscribeEvent
    public void onEntityUpdate (LivingUpdateEvent event) {
        
        if (!ASMHelper.isASMEnabled)
            Constants.LOG.warn("The ASM has not been initialized, there is an error with your setup!");
            
        else if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                armor.onHorseUpdate(horse, customArmor);
            }
        }
    }
    
    @SubscribeEvent
    public void onEntityHurt (LivingHurtEvent event) {
        
        if (!ASMHelper.isASMEnabled)
            Constants.LOG.warn("The ASM has not been initialized, there is an error with your setup!");
            
        else if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                event.setCanceled(armor.onHorseDamaged(horse, customArmor, event.source, event.ammount));
            }
        }
    }
    
    @SubscribeEvent
    public void onEntityConstructing (EntityEvent.EntityConstructing event) {
        
        if (event.entity instanceof EntityLivingBase && !EntityProperties.hasProperties((EntityLivingBase) event.entity))
            EntityProperties.setProperties((EntityLivingBase) event.entity);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld (EntityJoinWorldEvent event) {
        
        if (event.entity instanceof EntityLivingBase && !event.entity.worldObj.isRemote && EntityProperties.hasProperties((EntityLivingBase) event.entity))
            EntityProperties.getProperties((EntityLivingBase) event.entity).sync();
    }
    
    @SubscribeEvent
    public void handleBuffEntity (LivingUpdateEvent e) {
        
        EntityLivingBase entity = e.entityLiving;
        if (!entity.worldObj.isRemote) {
            List<BuffEffect> buffEffectList = BuffHelper.getEntityEffects(entity);
            for (int i = 0; i < buffEffectList.size(); i++) {
                BuffEffect buff = buffEffectList.get(i);
                if (buff.getBuff().canUpdate())
                    buff.getBuff().onBuffTick(entity.worldObj, entity, buff.duration, buff.power);
                buff.duration--;
                if (buff.duration <= 0) {
                    BuffHelper.getEntityEffects(entity).remove(i);
                }
                BuffHelper.updateBuff(entity.worldObj, entity, buff);
                Bookshelf.network.sendToAllAround(new PacketBuffUpdate(entity, buff), new NetworkRegistry.TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 128D));
            }
        }
    }
}

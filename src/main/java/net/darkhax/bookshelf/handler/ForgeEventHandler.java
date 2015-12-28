package net.darkhax.bookshelf.handler;

import java.util.Iterator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.buff.BuffEffect;
import net.darkhax.bookshelf.buff.BuffHelper;
import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.common.EntityProperties;
import net.darkhax.bookshelf.event.CreativeTabEvent;
import net.darkhax.bookshelf.event.PotionCuredEvent;
import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.*;

public class ForgeEventHandler {

    @SubscribeEvent
    public void onAnvilUsed (AnvilUpdateEvent event) {

        for (BookshelfRegistry.AnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes()) {

            if (recipe != null && ItemStackUtils.isValidStack(recipe.output) && ItemStackUtils.areStacksSimilarWithSize(event.left, recipe.inputLeft) && ItemStackUtils.areStacksSimilarWithSize(event.right, recipe.inputRight)) {

                event.cost = recipe.getExperienceCost(event.left, event.right, event.name);
                event.materialCost = recipe.getMaterialCost(event.left, event.right, event.name);

                if (recipe.nameTaxt != null && !recipe.nameTaxt.isEmpty()) {

                    if (recipe.nameTaxt.equalsIgnoreCase(event.name))
                        event.output = recipe.getOutput(event.left, event.right, event.name);

                    return;
                }

                event.output = recipe.getOutput(event.left, event.right, event.name);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onPotionsCured (PotionCuredEvent event) {

        BuffHelper.cureBuffs(event.entityLiving, event.stack);
    }

    @SubscribeEvent
    public void afterCreativeTabLoaded (CreativeTabEvent.Post event) {

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

        EntityLivingBase entity = event.entityLiving;


        List<BuffEffect> list = BuffHelper.getEntityEffects(entity);
        for (Iterator<BuffEffect> iterator = list.iterator(); iterator.hasNext();) {
            BuffEffect buff = iterator.next();
            if (buff.getBuff().canUpdate())
                buff.getBuff().onBuffTick(entity.worldObj, entity, buff.duration, buff.power);

            buff.duration--;
            if (!entity.worldObj.isRemote) {

                if (buff.duration <= 0) {

                    buff.getBuff().onEffectEnded();
                    EntityProperties.getProperties(entity).remove(buff, false);
                    iterator.remove();
                }
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
}

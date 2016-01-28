package net.darkhax.bookshelf.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.entity.EntityModPotion;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.MathsUtils;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModPotion extends Item {
    
    public static List<ItemStack> potionCache = new ArrayList<ItemStack>();
    
    public ItemModPotion() {
        
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }
    
    public static ItemStack createPotion (PotionEffect effect, boolean isSplash) {
        
        ItemStack stack = new ItemStack(Bookshelf.itemModPotion);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        NBTTagCompound potTag = effect.writeCustomPotionEffectToNBT(new NBTTagCompound());
        list.appendTag(potTag);
        tag.setTag("PotionEffects", list);
        tag.setBoolean("Splash", isSplash);
        stack.setTagCompound(tag);
        return stack;
    }
    
    public static List<PotionEffect> getEffects (ItemStack stack) {
        
        List<PotionEffect> effects = Lists.<PotionEffect> newArrayList();
        
        if (ItemStackUtils.isValidStack(stack) && stack.hasTagCompound() && stack.getTagCompound().hasKey("PotionEffects", 9)) {
            
            NBTTagList tagList = stack.getTagCompound().getTagList("PotionEffects", 10);
            
            for (int index = 0; index < tagList.tagCount(); index++) {
                
                NBTTagCompound effectTag = tagList.getCompoundTagAt(index);
                PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT(effectTag);
                
                if (effect != null)
                    effects.add(effect);
            }
        }
        
        return effects;
    }
    
    public static boolean isSplash (ItemStack stack) {
        
        return (ItemStackUtils.isValidStack(stack) && stack.hasTagCompound() && stack.getTagCompound().getBoolean("Splash"));
    }
    
    @Override
    public ItemStack onItemUseFinish (ItemStack stack, World world, EntityPlayer player) {
        
        if (!player.capabilities.isCreativeMode)
            --stack.stackSize;
            
        if (!world.isRemote) {
            
            List<PotionEffect> list = this.getEffects(stack);
            
            if (list != null)
                for (PotionEffect potioneffect : list)
                    player.addPotionEffect(new PotionEffect(potioneffect));
        }
        
        player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        
        if (!player.capabilities.isCreativeMode) {
            
            if (stack.stackSize <= 0)
                return new ItemStack(Items.glass_bottle);
                
            player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }
        
        return stack;
    }
    
    @Override
    public int getMaxItemUseDuration (ItemStack stack) {
        
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction (ItemStack stack) {
        
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player) {
        
        if (isSplash(stack)) {
            
            if (!player.capabilities.isCreativeMode)
                --stack.stackSize;
                
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            
            if (!world.isRemote)
                world.spawnEntityInWorld(new EntityModPotion(world, player, stack));
                
            player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return stack;
        }
        
        else {
            
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
            return stack;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage (int meta) {
        
        return PotionHelper.getLiquidColor(meta, false);
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack (ItemStack stack, int renderPass) {
        
        return renderPass > 0 ? 16777215 : this.getColorFromDamage(stack.getMetadata());
    }
    
    @Override
    public String getItemStackDisplayName (ItemStack stack) {
        
        List<PotionEffect> effects = getEffects(stack);
        
        if (!ItemStackUtils.isValidStack(stack) || !stack.hasTagCompound() || effects.isEmpty())
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
            
        String name = "";
        
        if (isSplash(stack))
            name = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
            
        String potionName = effects.get(0).getEffectName() + ".postfix";
        return name + StatCollector.translateToLocal(potionName).trim();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        
        for (PotionEffect effect : getEffects(stack)) {
            
            Potion pot = Utilities.getPotionByID(effect.getPotionID());
            
            if (pot != null) {
                
                String tip = "";
                tip += (pot.isBadEffect()) ? EnumChatFormatting.RED : EnumChatFormatting.GREEN;
                tip += StatCollector.translateToLocal(pot.getName());
                
                if (effect.getAmplifier() > 0)
                    tip += " " + StatCollector.translateToLocal("enchantment.level." + (effect.getAmplifier() + 1));
                    
                if (effect.getDuration() >= 20)
                    tip += " (" + MathsUtils.ticksToTime(effect.getDuration());
                    
                tooltip.add(tip);
            }
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect (ItemStack stack) {
        
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        
        super.getSubItems(itemIn, tab, subItems);
        
        if (potionCache.isEmpty()) {
            
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 900, 0), false));
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 2400, 0), false));
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 440, 1), false));
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 660, 0), true));
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 1800, 0), true));
            potionCache.add(createPotion(new PotionEffect(Potion.wither.id, 320, 1), true));
        }
        
        for (ItemStack stack : potionCache)
            subItems.add(stack.copy());
    }
}
package net.darkhax.bookshelf.client;

import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.darkhax.bookshelf.asm.ASMHelper;
import net.darkhax.bookshelf.items.ItemHorseArmor;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.Utilities;
import net.darkhax.bookshelf.potion.Buff;
import net.darkhax.bookshelf.potion.BuffEffect;
import net.darkhax.bookshelf.potion.BuffHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

public class RenderingHandler {
    
    private static final ResourceLocation INVENTORY_TEXTURES = new ResourceLocation("textures/gui/container/inventory.png");
    
    @SubscribeEvent
    public void onOverlayRender (GuiScreenEvent.DrawScreenEvent event) {
        
        if (event.gui != null && event.gui instanceof InventoryEffectRenderer) {
            
            InventoryEffectRenderer gui = (InventoryEffectRenderer) event.gui;
            int i = gui.guiLeft - 124;
            int j = gui.guiTop;
            EntityPlayer player = gui.mc.thePlayer;
            Collection<BuffEffect> effects = BuffHelper.getEntityEffects(player);
            
            if (!effects.isEmpty()) {
                
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                int yOffset = 33;
                int totalSize = effects.size() + player.getActivePotionEffects().size();
                
                if (player.getActivePotionEffects().size() > 5)
                    yOffset = 132 / (totalSize - 1);
                
                j += yOffset * (totalSize - 1);
                
                FontRenderer fnt = gui.mc.fontRenderer;
                
                for (Iterator<BuffEffect> iterator = effects.iterator(); iterator.hasNext(); j += yOffset) {
                    
                    BuffEffect buffEffect = iterator.next();
                    Buff buff = buffEffect.getBuff();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    gui.mc.getTextureManager().bindTexture(INVENTORY_TEXTURES);
                    gui.drawTexturedModalRect(i, j, 0, 166, 140, 32);
                    
                    buff.renderInventoryEffect(i, j, buffEffect, gui.mc);
                    if (!buff.shouldRenderInvText(buffEffect))
                        continue;
                    
                    String s1 = I18n.format(buff.getPotionName(), new Object[0]);
                    
                    if (buffEffect.power > 1)
                        s1 += " " + StatCollector.translateToLocal("enchantment.level." + buffEffect.power);
                    
                    fnt.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
                    fnt.drawStringWithShadow(StringUtils.ticksToElapsedTime(buffEffect.duration), i + 10 + 18, j + 6 + 10, 8355711);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Pre event) {
        
        triggerRenderHook(event, 0);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Post event) {
        
        triggerRenderHook(event, 1);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Specials.Pre event) {
        
        triggerRenderHook(event, 2);
    }
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Specials.Post event) {
        
        triggerRenderHook(event, 3);
    }
    
    /**
     * Attempts to trigger the rendering hook for a horses custom armor item. Will only work if
     * the entity is a horse, and is wearing ItemHorseArmor
     *
     * @param event: The event containing all the data we need to check if we should call the
     *            hook, and provide the hook with.
     * @param flag: An integer which represents the rendering stage. 0:pre 1:post 2:special-pre
     *            3:special-post
     */
    public void triggerRenderHook (RenderLivingEvent event, int flag) {
        
        if (!ASMHelper.isASMEnabled)
            Constants.LOG.warn("The ASM has not been initialized, there is an error with your setup!");
            
        else if (event.entity instanceof EntityHorse) {
            
            EntityHorse horse = (EntityHorse) event.entity;
            ItemStack customArmor = Utilities.getCustomHorseArmor(horse);
            
            if (customArmor != null && customArmor.getItem() instanceof ItemHorseArmor) {
                
                ItemHorseArmor armor = (ItemHorseArmor) customArmor.getItem();
                armor.onArmorRendering(horse, customArmor, event.renderer, event.x, event.y, event.z, flag);
            }
        }
    }
}
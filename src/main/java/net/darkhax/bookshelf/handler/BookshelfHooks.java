package net.darkhax.bookshelf.handler;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Event;
import net.darkhax.bookshelf.event.ItemEnchantedEvent;
import net.darkhax.bookshelf.event.MobSpawnerSpawnEvent;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class BookshelfHooks {
    
    /**
     * A hook for triggering the ItemEnchantedEvent. This hook is only triggered from
     * ContainerEnchantment, however it is possible to integrate this hook into other mods and
     * enchanting methods.
     *
     * @param player: The player that is enchanting the item.
     * @param stack: The item that is being enchanted.
     * @param levels: The amount of levels consumed by the enchantment.
     * @param enchantments: A list of enchantments being added to the ItemStack.
     * @return List<EnchantmentData>: A list of enchantments to add to the ItemStack.
     */
    public static List<EnchantmentData> onItemEnchanted (EntityPlayer player, ItemStack stack, int levels, List<EnchantmentData> enchantments) {
        
        if (enchantments != null) {
            
            ItemEnchantedEvent event = new ItemEnchantedEvent(player, stack, levels, enchantments);
            MinecraftForge.EVENT_BUS.post(event);
            return event.enchantments;
        }
        
        return enchantments;
    }
    
    /**
     * A hook to trigger the MobSpawnerSpawnEvent. This hook is only triggered by the vanilla
     * monster spawner, however it is possible for other mods to also trigger this event.
     * 
     * @param entity: An instance of the entity that is attempting to spawn.
     * @return boolean: Whether or not the event should be allowed to happen.
     */
    public static boolean doEntitySpawnSpawnerCheck (Entity entity) {
        
        MobSpawnerSpawnEvent event = new MobSpawnerSpawnEvent(entity);
        MinecraftForge.EVENT_BUS.post(event);
        Event.Result result = event.getResult();
        return (result == Event.Result.DEFAULT) ? !(entity instanceof EntityLiving) || ((EntityLiving) entity).getCanSpawnHere() : result == Event.Result.ALLOW;
    }
}
package net.darkhax.bookshelf.handler;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Event;
import net.darkhax.bookshelf.event.ItemEnchantedEvent;
import net.darkhax.bookshelf.event.MobSpawnerSpawnEvent;
import net.darkhax.bookshelf.event.PistonEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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
    
    public static boolean doEntitySpawnSpawnerCheck (Entity entity) {
        
        MobSpawnerSpawnEvent event = new MobSpawnerSpawnEvent(entity);
        MinecraftForge.EVENT_BUS.post(event);
        Event.Result result = event.getResult();
        
        if (result == Event.Result.DEFAULT)
            return !(entity instanceof EntityLiving) || ((EntityLiving) entity).getCanSpawnHere();
        else
            return result == Event.Result.ALLOW;
    }
    
    public static boolean onPistonExtend (int x, int y, int z, World world, Block block, int blockMetadata, int facing, boolean sticky) {
        
        return MinecraftForge.EVENT_BUS.post(new PistonEvent.PistonExtendEvent(x, y, z, world, block, blockMetadata, sticky, EnumFacing.values()[facing]));
    }
    
    public static boolean onPistonRetract (int x, int y, int z, World world, Block block, int blockMetadata, int facing, boolean sticky) {
        
        return MinecraftForge.EVENT_BUS.post(new PistonEvent.PistonRetractEvent(x, y, z, world, block, blockMetadata, sticky, EnumFacing.values()[facing]));
    }
    
    public static boolean canPushBlock (Block pushedBlock, World world, int x, int y, int z) {
        
        Block block = world.getBlock(x, y, z);
        if (block != null && block instanceof BlockPistonBase) {
            int meta = world.getBlockMetadata(x, y, z);
            boolean sticky = block == Blocks.sticky_piston;
            PistonEvent.PistonPushEvent event = new PistonEvent.PistonPushEvent(block, world, x, y, z, meta, sticky, pushedBlock, EnumFacing.values()[meta & 7]);
            MinecraftForge.EVENT_BUS.post(event);
            Event.Result result = event.getResult();
            
            return result != Event.Result.DEFAULT && result == Event.Result.ALLOW;
        }
        return false;
    }
}
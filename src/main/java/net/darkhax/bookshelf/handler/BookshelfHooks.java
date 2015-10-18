package net.darkhax.bookshelf.handler;

import cpw.mods.fml.common.eventhandler.Event;
import net.darkhax.bookshelf.event.ItemEnchantedEvent;
import net.darkhax.bookshelf.event.MobSpawnerSpawnEvent;
import net.darkhax.bookshelf.event.PistonEvent;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class BookshelfHooks {

    public static boolean doEntitySpawnSpawnerCheck (Entity entity, EntityLiving living) {

        MobSpawnerSpawnEvent event = new MobSpawnerSpawnEvent(entity, living);
        MinecraftForge.EVENT_BUS.post(event);
        Event.Result result = event.getResult();

        if (result == Event.Result.DEFAULT)
            return living == null || living.getCanSpawnHere();
        else
            return result == Event.Result.ALLOW;
    }

    /**
     * A hook for triggering the ItemEnchantedEvent. This hook is only triggered from
     * ContainerEnchantment, however it is possible to integrate this hook into other mods and
     * enchanting methods.
     *
     * @param player:       The player that is enchanting the item.
     * @param stack:        The item that is being enchanted.
     * @param levels:       The amount of levels consumed by the enchantment.
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

    public static boolean onPistonExtend (int x, int y, int z, World world, Block block, int blockMetadata, int facing) {
        System.out.println(MinecraftForge.EVENT_BUS.post(new PistonEvent.PistonExtendEvent(x, y, z, world, block, blockMetadata, EnumFacing.values()[facing])));
        return !MinecraftForge.EVENT_BUS.post(new PistonEvent.PistonExtendEvent(x, y, z, world, block, blockMetadata, EnumFacing.values()[facing]));
    }

    public static boolean onPistonRetract (int x, int y, int z, World world, Block block, int blockMetadata, int facing) {

        return !MinecraftForge.EVENT_BUS.post(new PistonEvent.PistonRetractEvent(x, y, z, world, block, blockMetadata, EnumFacing.values()[facing]));
    }
}

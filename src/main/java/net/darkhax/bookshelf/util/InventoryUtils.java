/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Random;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class InventoryUtils {
    
    /**
     * Gets an inventory from a position within the world. If no tile exists or the tile does
     * not have the inventory capability the empty inventory handler will be returned.
     * 
     * @param world The world instance.
     * @param pos The position of the expected tile entity.
     * @param side The side to access the inventory from.
     * @return The inventory handler. Will be empty if none was found.
     */
    public static IItemHandler getInventory (World world, BlockPos pos, Direction side) {
        
        final TileEntity tileEntity = world.getTileEntity(pos);
        
        if (tileEntity != null) {
            
            final LazyOptional<IItemHandler> inventoryCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            return inventoryCap.orElse(EmptyHandler.INSTANCE);
        }
        
        else {
            
            // Some blocks like composters are not tile entities so their inv can not be
            // accessed through the normal capability system.
            final BlockState state = world.getBlockState(pos);
            
            if (state.getBlock() instanceof ISidedInventoryProvider) {
                
                final ISidedInventoryProvider inventoryProvider = (ISidedInventoryProvider) state.getBlock();
                final ISidedInventory inventory = inventoryProvider.createInventory(state, world, pos);
                
                if (inventory != null) {
                    
                    return new SidedInvWrapper(inventory, side);
                }
            }
        }
        
        return EmptyHandler.INSTANCE;
    }
    
    /**
     * Checks if a position has an inventory. The empty inventory handler will be considered
     * invalid.
     * 
     * @param world The world instance.
     * @param pos The position of the expected tile entity.
     * @param side The side to access the inventory from.
     * @return Whether or not the inventory exists.
     */
    public static boolean hasInventory (World world, BlockPos pos, Direction side) {
        
        final IItemHandler handler = getInventory(world, pos, side);
        return handler != null && handler != EmptyHandler.INSTANCE;
    }
    
    /**
     * Gets the container backing a CraftingInventory. This is normally private however it has
     * been made accessible through an access transformer.
     * 
     * @param craftingInv The crafting inventory.
     * @return The container backing the crafting inventory.
     */
    @Nullable
    public static Container getCraftingContainer (CraftingInventory craftingInv) {
        
        return craftingInv.eventHandler;
    }
    
    @Nullable
    public static PlayerEntity getCraftingPlayer (IInventory inventory) {
        
        return getCraftingPlayer(inventory, null);
    }
    
    /**
     * Attempts to locate a player from an inventory. This method is specifically intended for
     * crafting related inventories and is not guaranteed to work with every or even most
     * inventories or containers.
     * 
     * @param inventory The inventory to search.
     * @param world The world instance.
     * @return The player that was found. This may be null if no player could be found.
     */
    @Nullable
    public static PlayerEntity getCraftingPlayer (IInventory inventory, World world) {
        
        if (inventory instanceof PlayerInventory) {
            
            return ((PlayerInventory) inventory).player;
        }
        
        else if (inventory instanceof CraftingInventory) {
            
            final Container container = getCraftingContainer((CraftingInventory) inventory);
            
            if (container instanceof WorkbenchContainer) {
                
                return ((WorkbenchContainer) container).player;
            }
            
            else if (container instanceof PlayerContainer) {
                
                return ((PlayerContainer) container).player;
            }
        }
        
        // TODO add a way for other mods to add special handling for their inventories and
        // containers if they want to.
        return null;
    }
    
    /**
     * An extension of the IRecipe getRemainingItems method which attempts to keep items that
     * have durability. Instead of being consumed these items will attempt to have their
     * durability decreased. Things like unbreaking and unbreakable nbt settings are considered
     * depending on the input arguments.
     * 
     * @param inv The inventory doing the crafting.
     * @param keptItems The list of items being kept.
     * @param ignoreUnbreaking Whether or not unbreaking enchantments should be ignored.
     * @param damageAmount The amount of damage to set on the item.
     * @return The list of items being kept.
     */
    public static NonNullList<ItemStack> keepDamageableItems (CraftingInventory inv, NonNullList<ItemStack> keptItems, boolean ignoreUnbreaking, int damageAmount) {
        
        for (int i = 0; i < keptItems.size(); i++) {
            
            final ItemStack stack = inv.getStackInSlot(i);
            
            // Checks if the item has durability or has the unbreaking tag.
            if (stack.getItem().isDamageable() || stack.hasTag() && stack.getTag().getBoolean("Unbreakable")) {
                
                @Nullable
                final PlayerEntity player = InventoryUtils.getCraftingPlayer(inv);
                final Random random = player != null ? player.getRNG() : Bookshelf.RANDOM;
                final ItemStack retainedStack = stack.copy();
                
                // Sometimes you may want to ignore/bypass the unbreaking enchantment.
                if (ignoreUnbreaking) {
                    
                    // Checks if the stack can be damaged. If so continue and bypass all the
                    // other item damaging mechanics.
                    if (retainedStack.isDamageable()) {
                        
                        retainedStack.setDamage(retainedStack.getDamage() + damageAmount);
                    }
                }
                
                else {
                    
                    // Attempts to damage the item, taking things like the unbreaking
                    // enchantment into consideration.
                    retainedStack.attemptDamageItem(damageAmount, random, player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
                }
                
                keptItems.set(i, retainedStack);
            }
        }
        
        return keptItems;
    }
}
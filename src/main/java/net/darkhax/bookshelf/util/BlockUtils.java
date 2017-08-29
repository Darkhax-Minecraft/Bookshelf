/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

public final class BlockUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an implicit
     * public constructor to every class which does not define at lease one explicitly. Hence why
     * this constructor was added.
     */
    private BlockUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Checks if an ItemStack contains an ore item. This is done by checking the item extends
     * BlockOre, or if the ore dictionary for entries that start with 'ore'. It will also check the
     * display name of the stack to see if it has the word Ore in it.
     *
     * @param stack The ItemStack to check.
     * @param checkName Whether or not the name of the ItemStack should be checked.
     * @return Whether or not the ItemStack is an ore.
     */
    public static boolean isOre (@Nonnull ItemStack stack, boolean checkName) {

        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockOre) {
            return true;
        }

        for (final int oreID : OreDictionary.getOreIDs(stack)) {
            if (OreDictionary.getOreName(oreID).startsWith("ore")) {
                return true;
            }
        }

        if (checkName && stack.getItem().getItemStackDisplayName(stack).matches(".*(^|\\s)([oO]re)($|\\s).")) {
            return true;
        }

        return false;
    }

    /**
     * Checks if a block is a fluid or not.
     *
     * @param block An instance of the block being checked.
     * @return If the block is a fluid, true will be returned. If not, false will be returned.
     */
    public static boolean isFluid (Block block) {

        return block == Blocks.LAVA || block == Blocks.WATER || block instanceof IFluidBlock;
    }

    /**
     * Checks if the fluid level is full.
     *
     * @param world The world.
     * @param pos The position.
     * @return Whether or not the fluid is full.
     */
    public static boolean isFluidFull (World world, BlockPos pos) {

        return isFluidFull(getActualState(world, pos));
    }

    /**
     * Checks if the fluid level is full.
     *
     * @param state The block state.
     * @return Whether or not the fluid is full.
     */
    public static boolean isFluidFull (IBlockState state) {

        return getFluidLevel(state) == 0;
    }

    /**
     * Get the fluid level.
     *
     * @param world The world.
     * @param pos The position.
     * @return The level of the fluid. 0 is full.
     */
    public static int getFluidLevel (World world, BlockPos pos) {

        return getFluidLevel(getActualState(world, pos));
    }

    /**
     * Get the fluid level.
     *
     * @param state The block state.
     * @return The level of the fluid. 0 is full.
     */
    public static int getFluidLevel (IBlockState state) {

        return state.getValue(BlockLiquid.LEVEL);
    }

    /**
     * Attempts to get a fluid stack from a block pos.
     *
     * @param world The world.
     * @param pos The position.
     * @return The fluid stack.
     */
    public static FluidStack getFluid (World world, BlockPos pos) {

        final IBlockState state = getActualState(world, pos);
        final Block block = state.getBlock();

        if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, pos)) {

            return ((IFluidBlock) block).drain(world, pos, true);
        }

        else if (block instanceof BlockStaticLiquid && isFluidFull(state)) {

            final Fluid fluid = block == Blocks.WATER ? FluidRegistry.WATER : block == Blocks.LAVA ? FluidRegistry.LAVA : null;

            if (fluid != null) {

                return new FluidStack(fluid, 1000);
            }
        }

        return null;
    }

    /**
     * Gets the actual state of the block.
     *
     * @param world The world.
     * @param pos The position.
     * @return The actual block state.
     */
    public static IBlockState getActualState (World world, BlockPos pos) {

        return world.getBlockState(pos).getActualState(world, pos);
    }
}
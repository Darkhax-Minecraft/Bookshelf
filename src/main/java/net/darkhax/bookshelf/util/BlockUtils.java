/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import javax.annotation.Nonnull;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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

    /**
     * Creates a modified version of the break speed which already has the hardness and harvest
     * check factored in. This allows for the BreakSpeed event to change the final speed value, by
     * adjusting for the hardness and other whether or not the player has the right tool.
     *
     * @param desiredBreakSpeed The desired break speed. This is the desired final result.
     * @param currentHardness The current hardness of the block.
     * @param canHarvest Whether or not the current block can be harvested.
     * @return A modified version of the break speed with adjustments for hardness and
     *         harvestability.
     */
    public static float getModifiedBreakSpeed (float desiredBreakSpeed, float currentHardness, boolean canHarvest) {

        float speed = desiredBreakSpeed;
        speed *= currentHardness;
        speed *= canHarvest ? 30f : 100f;
        return speed;
    }

    /**
     * Checks if the player can harvest a block, without risk of crashes due to unsupported use of
     * the method.
     *
     * @param state The block state to check.
     * @param player The player to try and harvest the block.
     * @return Whether or not the block can be harvested.
     */
    public static boolean canHarvestSafely (IBlockState state, EntityPlayer player) {

        try {

            final Block block = state.getBlock();

            if (state.getMaterial().isToolNotRequired()) {

                return true;
            }

            final ItemStack stack = player.getHeldItemMainhand();
            final String tool = block.getHarvestTool(state);

            if (stack.isEmpty() || tool == null) {

                return player.canHarvestBlock(state);
            }

            final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);

            if (toolLevel < 0) {

                return player.canHarvestBlock(state);
            }

            return toolLevel >= block.getHarvestLevel(state);
        }

        catch (final Exception e) {

            Constants.LOG.trace("Error checking harvest for " + state.toString(), e);
            return false;
        }
    }

    /**
     * Gets the hardness of a block without risk of a crash due to unsupported use.
     *
     * @param state The block state to check.
     * @param world The world object.
     * @param pos The position of the block.
     * @return The hardness of the block. If an exception occurs this will be 99999f.
     */
    public static float getHardnessSafely (IBlockState state, World world, BlockPos pos) {

        try {

            return state.getBlock().getBlockHardness(state, world, pos);
        }

        catch (final Exception e) {

            Constants.LOG.trace("Error checking hardness for " + state.toString(), e);
            // TODO better fallback code
            return 99999f;
        }
    }

    /**
     * Calculates the break speed for a block.
     *
     * @param playerDigSpeed The player's dig speed.
     * @param hardness The hardness of the block.
     * @param canHarvest Whether or not the block can be harvested.
     * @return The break speed for a block.
     */
    public static float getBreakSpeed (float playerDigSpeed, float hardness, boolean canHarvest) {

        return playerDigSpeed / hardness / (canHarvest ? 30f : 100f);
    }

    /**
     * Gets the block without risk of crash from unsupported use.
     *
     * @param state The block state to get strength of.
     * @param player The player mining the block.
     * @param world The world object.
     * @param pos The position of the block.
     * @return The strength of the block.
     */
    public static float blockStrengthSafely (@Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos) {

        try {

            final float hardness = getHardnessSafely(state, world, pos);

            if (hardness < 0f) {

                return 0;
            }

            return player.getDigSpeed(state, pos) / hardness / (canHarvestSafely(state, player) ? 30f : 100f);
        }

        catch (final Exception e) {

            Constants.LOG.trace("Exception getting block strength! " + state.toString(), e);
            return 99999f;
        }
    }

    /**
     * This method is used to calculate and replicate the break speed of another block, in the
     * BreakSpeed event. This will take all the required values into effect and give you a float
     * that you can use in the event.
     *
     * @param current The block state that you want to replace the speed of.
     * @param target The block state that you want to replace the current state with.
     * @param world The world object.
     * @param player The player that is mining the block.
     * @param pos The position of the block.
     * @return A modified speed value that can be used in BreakSpeed to replace the speed of one
     *         block with the speed of another.
     */
    public static float getBreakSpeedToMatch (IBlockState current, IBlockState target, World world, EntityPlayer player, BlockPos pos) {

        final float currentHardness = current.getBlockHardness(world, pos);
        final boolean currentCanHarvest = canHarvestSafely(current, player);
        final float targetSpeed = blockStrengthSafely(target, player, world, pos);
        final float currentSpeed = getModifiedBreakSpeed(targetSpeed, currentHardness, currentCanHarvest);
        return currentSpeed;
    }

    /**
     * Checks if a block position is in a slime chunk.
     *
     * @param world The world instance.
     * @param pos The position to check.
     * @return Whether or not the chunk is a slime chunk.
     */
    public static boolean isSlimeChunk (World world, BlockPos pos) {

        return world.getChunkFromBlockCoords(pos).getRandomWithSeed(987234911L).nextInt(10) == 0;
    }
}
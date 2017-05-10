package net.darkhax.bookshelf.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockStrengthEvent extends Event {

    private final IBlockState state;

    private final EntityPlayer player;

    private final World world;

    private final BlockPos pos;

    private float strength;

    // @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull
    // BlockPos pos
    public BlockStrengthEvent (IBlockState state, EntityPlayer player, World world, BlockPos pos) {

        this.state = state;
        this.player = player;
        this.world = world;
        this.pos = pos;
        this.strength = 0.0f;
    }

    public float getStrength () {

        return this.strength;
    }

    public void setStrength (float strength) {

        this.strength = strength;
    }

    public IBlockState getState () {

        return this.state;
    }

    public EntityPlayer getPlayer () {

        return this.player;
    }

    public World getWorld () {

        return this.world;
    }

    public BlockPos getPos () {

        return this.pos;
    }

    public float getNaturalStrength (IBlockState state) {

        final float hardness = state.getBlockHardness(this.world, this.pos);
        return hardness < 0.0f ? 0.0f : !ForgeHooks.canHarvestBlock(state.getBlock(), this.player, this.world, this.pos) ? this.player.getDigSpeed(state, this.pos) / hardness / 100F : this.player.getDigSpeed(state, this.pos) / hardness / 30F;
    }
}

/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockEvent extends Event {

    /**
     * The world instance.
     */
    private final IBlockAccess world;

    /**
     * The position of the block.
     */
    private final BlockPos pos;

    /**
     * The model to render.
     */
    private IBakedModel model;

    /**
     * The state data.
     */
    private IBlockState state;

    /**
     * The vertex buffer.
     */
    private VertexBuffer buffer;

    /**
     * Whether or not the sides should be changed.
     */
    private boolean checkSides;

    /**
     * Constructs the event.
     *
     * @param world The world instance.
     * @param pos The position of the block.
     * @param model The model to render.
     * @param state The state data.
     * @param buffer The vertex buffer.
     * @param checkSides Whether or not the sides should be checked.
     */
    public RenderBlockEvent (IBlockAccess world, BlockPos pos, IBakedModel model, IBlockState state, VertexBuffer buffer, boolean checkSides) {

        this.world = world;
        this.pos = pos;
        this.model = model;
        this.state = state;
        this.buffer = buffer;
        this.checkSides = checkSides;
    }

    public IBakedModel getModel () {

        return this.model;
    }

    public void setModel (IBakedModel model) {

        this.model = model;
    }

    public IBlockState getState () {

        return this.state;
    }

    public void setState (IBlockState state) {

        this.state = state;
    }

    public VertexBuffer getBuffer () {

        return this.buffer;
    }

    public void setBuffer (VertexBuffer buffer) {

        this.buffer = buffer;
    }

    public boolean isCheckSides () {

        return this.checkSides;
    }

    public void setCheckSides (boolean checkSides) {

        this.checkSides = checkSides;
    }

    public IBlockAccess getWorld () {

        return this.world;
    }

    public BlockPos getPos () {

        return this.pos;
    }
}
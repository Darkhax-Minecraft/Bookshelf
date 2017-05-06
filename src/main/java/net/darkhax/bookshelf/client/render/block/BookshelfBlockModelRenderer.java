/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.common.MinecraftForge;

public class BookshelfBlockModelRenderer extends ForgeBlockModelRenderer {

    /**
     * A reference to the Minecraft instance.
     */
    private static Minecraft mc;

    /**
     * Whether or not the bookshelf model renderer has already been initialized.
     */
    private static boolean initialized;

    /**
     * The BlockModelRenderer that was there before this one.
     */
    private static BlockModelRenderer parent;

    /**
     * Creates a new instance of the BookshelfBlockModelRenderer. If the renderer has already
     * been created, you will get the current renderer instead.
     *
     * @return The current BlockModelRenderer.
     */
    public static BlockModelRenderer instance () {

        mc = Minecraft.getMinecraft();
        final BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();

        if (!initialized) {

            dispatcher.blockModelRenderer = new BookshelfBlockModelRenderer(dispatcher.blockModelRenderer);
            initialized = true;
        }

        return dispatcher.blockModelRenderer;
    }

    private BookshelfBlockModelRenderer (BlockModelRenderer parentIn) {

        super(Minecraft.getMinecraft().getBlockColors());
        parent = parentIn;
    }

    @Override
    public boolean renderModelSmooth (IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, VertexBuffer buffer, boolean checkSides, long rand) {

        final RenderBlockEvent event = new RenderBlockEvent(worldIn, posIn, modelIn, stateIn, buffer, checkSides);
        MinecraftForge.EVENT_BUS.post(event);
        return parent.renderModelSmooth(worldIn, event.getModel(), event.getState(), posIn, event.getBuffer(), event.isCheckSides(), rand);
    }

    @Override
    public boolean renderModelFlat (IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, VertexBuffer buffer, boolean checkSides, long rand) {

        final RenderBlockEvent event = new RenderBlockEvent(worldIn, posIn, modelIn, stateIn, buffer, checkSides);
        MinecraftForge.EVENT_BUS.post(event);
        return parent.renderModelSmooth(worldIn, event.getModel(), event.getState(), posIn, event.getBuffer(), event.isCheckSides(), rand);
    }
}

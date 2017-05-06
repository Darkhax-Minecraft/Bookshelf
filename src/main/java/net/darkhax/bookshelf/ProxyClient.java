/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.client.render.RenderBasicChest;
import net.darkhax.bookshelf.client.render.item.RenderFactoryItem;
import net.darkhax.bookshelf.entity.EntityFake;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.features.FeatureManager;
import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {

    @Override
    public void preInit () {

        for (final Feature feature : FeatureManager.getFeatures()) {
            feature.onClientPreInit();
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new RenderBasicChest());
        RenderingRegistry.registerEntityRenderingHandler(EntityFake.class, new RenderFactoryItem());
    }
}
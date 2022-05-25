package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.block.entity.WorldlyInventoryBlockEntity;
import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.darkhax.bookshelf.impl.capabilities.SingletonCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class BookshelfForge {

    public BookshelfForge() {

        new BookshelfCommon();

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::attachBlockCapabilities);
    }

    private static final ResourceLocation WORLDLY_CONTAINER_WRAPPER = new ResourceLocation(Constants.MOD_ID, "worldly_container_wrapper");

    private void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {

        if (event.getObject() instanceof WorldlyInventoryBlockEntity<?> worldly) {

            event.addCapability(WORLDLY_CONTAINER_WRAPPER, SingletonCapabilityProvider.of(worldly));
        }
    }
}
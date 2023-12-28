package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.block.entity.WorldlyInventoryBlockEntity;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@Mod(Constants.MOD_ID)
public class BookshelfNeoForge {

    public BookshelfNeoForge() {

        new BookshelfCommon();
        NeoForge.EVENT_BUS.addListener(this::attachBlockCapabilities);
    }

    private static final ResourceLocation WORLDLY_CONTAINER_WRAPPER = Constants.id("worldly_container_wrapper");
    private static final BlockCapability<IItemHandler, Direction> WRAPPER_CAP = BlockCapability.createSided(WORLDLY_CONTAINER_WRAPPER, IItemHandler.class);

    private void attachBlockCapabilities(RegisterCapabilitiesEvent event) {

        for (BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {

            if (IConstructHelper.TYPE_CLASSES.containsKey(type)) {

                final Class<?> blockEntityClass = IConstructHelper.TYPE_CLASSES.get(type);

                if (WorldlyInventoryBlockEntity.class.isAssignableFrom(blockEntityClass)) {

                    event.registerBlockEntity(WRAPPER_CAP, type, (blockEntity, side) -> {

                        if (blockEntity instanceof WorldlyInventoryBlockEntity<?> worldly) {

                            return side != null ? new SidedInvWrapper(worldly, side) : new InvWrapper(worldly);
                        }
                        
                        return null;
                    });
                }
            }
        }
    }
}
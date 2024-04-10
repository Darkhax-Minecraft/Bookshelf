package net.darkhax.bookshelf;

import net.darkhax.bookshelf.api.block.entity.WorldlyInventoryBlockEntity;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@Mod(Constants.MOD_ID)
public class BookshelfNeoForge {

    public BookshelfNeoForge(IEventBus modBus) {

        new BookshelfCommon();
        modBus.addListener(this::attachBlockCapabilities);
    }

    private void attachBlockCapabilities(RegisterCapabilitiesEvent event) {

        for (BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {

            if (IConstructHelper.TYPE_CLASSES.containsKey(type)) {

                final Class<?> blockEntityClass = IConstructHelper.TYPE_CLASSES.get(type);

                if (WorldlyInventoryBlockEntity.class.isAssignableFrom(blockEntityClass)) {

                    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (blockEntity, side) -> {

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
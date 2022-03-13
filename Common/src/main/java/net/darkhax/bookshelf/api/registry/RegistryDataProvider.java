package net.darkhax.bookshelf.api.registry;

import net.darkhax.bookshelf.api.ClientServices;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.block.IBindRenderLayer;
import net.darkhax.bookshelf.api.block.IItemBlockProvider;
import net.darkhax.bookshelf.api.commands.ICommandBuilder;
import net.darkhax.bookshelf.mixin.item.AccessorItem;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class RegistryDataProvider {

    private final String ownerId;

    public final IOwnedRegistryEntries<Block> blocks = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Fluid> fluids = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Item> items = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<MobEffect> mobEffects = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<SoundEvent> sounds = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Potion> potions = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Enchantment> enchantments = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<EntityType<?>> entities = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<BlockEntityType<?>> blockEntities = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<ParticleType<?>> particleTypes = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<MenuType<?>> menus = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<RecipeSerializer<?>> recipeSerializers = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Motive> paintings = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<Attribute> attributes = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<StatType<?>> stats = new RegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<VillagerProfession> villagerProfessions = new RegistryEntries<>(this::getOwner);

    public final ClassRegistryEntries<ArgumentSerializer> commandArguments = new ClassRegistryEntries<>(this::getOwner);
    public final IOwnedRegistryEntries<ICommandBuilder> commands = new RegistryEntries<>(this::getOwner);

    public RegistryDataProvider(String ownerId) {

        this.ownerId = ownerId;
    }

    public final RegistryDataProvider withCreativeTab(Supplier<ItemLike> iconProvider) {

        return this.withCreativeTab(iconProvider, "creative_tab");
    }

    public final RegistryDataProvider withCreativeTab(Supplier<ItemLike> iconProvider, String id) {

        return this.withCreativeTab(Services.CREATIVE_TABS.createBuilder(this.ownerId, id).setIcon(iconProvider).build());
    }

    public final RegistryDataProvider withCreativeTab(CreativeModeTab tab) {

        this.items.addRegistryListener((id, item) -> {

            if (item instanceof AccessorItem accessor) {

                accessor.bookshelf$setCreativeTab(tab);
            }
        });

        return this;
    }

    public final RegistryDataProvider bindBlockRenderLayers() {

        if (Services.PLATFORM.isPhysicalClient()) {

            this.blocks.addRegistryListener((id, block) -> {

                if (block instanceof IBindRenderLayer binder) {

                    ClientServices.CLIENT.setRenderType(block, binder.getRenderLayerToBind());
                }
            });
        }

        return this;
    }

    public final RegistryDataProvider withAutoItemBlocks() {

        this.blocks.addRegistryListener((id, block) -> {

            this.items.add(() -> {

                if (block instanceof IItemBlockProvider provider) {

                    return provider.createItemBlock(block);
                }

                return IItemBlockProvider.DEFAULT.createItemBlock(block);
            }, id);
        });

        return this;
    }

    public String getOwner() {

        return this.ownerId;
    }
}

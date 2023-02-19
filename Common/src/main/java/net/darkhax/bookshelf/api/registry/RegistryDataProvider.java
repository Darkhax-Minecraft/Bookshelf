package net.darkhax.bookshelf.api.registry;

import net.darkhax.bookshelf.api.ClientServices;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.block.IBindRenderLayer;
import net.darkhax.bookshelf.api.block.IItemBlockProvider;
import net.darkhax.bookshelf.api.commands.ICommandBuilder;
import net.darkhax.bookshelf.api.item.tab.ITabBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistryDataProvider {

    private final String ownerId;

    public final IOwnedRegistryEntries<Block> blocks = new RegistryEntries<>(this::getOwner, Registries.BLOCK);
    public final IOwnedRegistryEntries<Fluid> fluids = new RegistryEntries<>(this::getOwner, Registries.FLUID);
    public final IOwnedRegistryEntries<Item> items = new RegistryEntries<>(this::getOwner, Registries.ITEM);
    public final IOwnedRegistryEntries<BannerPattern> bannerPatterns = new RegistryEntries<>(this::getOwner, Registries.BANNER_PATTERN);
    public final IOwnedRegistryEntries<MobEffect> mobEffects = new RegistryEntries<>(this::getOwner, Registries.MOB_EFFECT);
    public final IOwnedRegistryEntries<SoundEvent> sounds = new RegistryEntries<>(this::getOwner, Registries.SOUND_EVENT);
    public final IOwnedRegistryEntries<Potion> potions = new RegistryEntries<>(this::getOwner, Registries.POTION);
    public final IOwnedRegistryEntries<Enchantment> enchantments = new RegistryEntries<>(this::getOwner, Registries.ENCHANTMENT);
    public final IOwnedRegistryEntries<EntityType<?>> entities = new RegistryEntries<>(this::getOwner, Registries.ENTITY_TYPE);
    public final IOwnedRegistryEntries<BlockEntityType<?>> blockEntities = new RegistryEntries<>(this::getOwner, Registries.BLOCK_ENTITY_TYPE);
    public final IOwnedRegistryEntries<ParticleType<?>> particleTypes = new RegistryEntries<>(this::getOwner, Registries.PARTICLE_TYPE);
    public final IOwnedRegistryEntries<MenuType<?>> menus = new RegistryEntries<>(this::getOwner, Registries.MENU);
    public final IOwnedRegistryEntries<RecipeSerializer<?>> recipeSerializers = new RegistryEntries<>(this::getOwner, Registries.RECIPE_SERIALIZER);
    public final IOwnedRegistryEntries<PaintingVariant> paintings = new RegistryEntries<>(this::getOwner, Registries.PAINTING_VARIANT);
    public final IOwnedRegistryEntries<Attribute> attributes = new RegistryEntries<>(this::getOwner, Registries.ATTRIBUTE);
    public final IOwnedRegistryEntries<StatType<?>> stats = new RegistryEntries<>(this::getOwner, Registries.STAT_TYPE);
    public final IOwnedRegistryEntries<VillagerProfession> villagerProfessions = new RegistryEntries<>(this::getOwner, Registries.VILLAGER_PROFESSION);
    public final CommandArgumentRegistryEntries commandArguments = new CommandArgumentRegistryEntries(this::getOwner, Registries.COMMAND_ARGUMENT_TYPE);
    public final RecipeTypeEntries recipeTypes = new RecipeTypeEntries(this::getOwner);
    public final IOwnedRegistryEntries<ICommandBuilder> commands = new RegistryEntries<>(this::getOwner, "Command");
    public final VillagerTradeEntries trades = new VillagerTradeEntries();
    public final IOwnedRegistryEntries<PreparableReloadListener> resourceListeners = new RegistryEntries<>(this::getOwner, "Resource Listener");
    public final IOwnedRegistryEntries<PreparableReloadListener> dataListeners = new RegistryEntries<>(this::getOwner, "Data Listener");
    public final IOwnedRegistryEntries<Consumer<ITabBuilder>> creativeTabs = new RegistryEntries<>(this::getOwner, "Creative Tabs");

    public RegistryDataProvider(String ownerId) {

        this.ownerId = ownerId;
    }

    public final RegistryDataProvider withItemTab(Supplier<ItemStack> icon) {

        this.creativeTabs.add(() -> builder -> {
            builder.icon(icon);
            builder.displayItems((params, output) -> output.acceptItemIter(this.items));
        }, "creative_tab");
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

        // This code is invoked once for each block as it is being registered. At this stage
        // the actual block instance has been resolved.
        this.blocks.addRegistryListener((id, block) -> {

            // Allow blocks to specify custom itemblock behaviour
            if (block instanceof IItemBlockProvider provider) {

                // Returning false here will prevent an ItemBlock from being created.
                if (provider.hasItemBlock(block)) {

                    this.items.add(() -> provider.createItemBlock(block), id);
                }
            }

            // The default behaviour just makes a normal ItemBlock.
            else {

                this.items.add(() -> IItemBlockProvider.DEFAULT.createItemBlock(block), id);
            }
        });

        return this;
    }

    public String getOwner() {

        return this.ownerId;
    }
}

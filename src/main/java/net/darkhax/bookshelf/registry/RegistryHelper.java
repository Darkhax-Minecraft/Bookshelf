package net.darkhax.bookshelf.registry;

import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.block.IBookshelfBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;

public class RegistryHelper {

    public final String modid;

    public final CommandRegistry commands;
    public final TradeRegistry trades;
    public final RecipeTypeRegistry recipeTypes;
    public final IngredientRegistry ingredients;
    public final ForgeRegistryRegistryHelper registries;
    public final BannerRegistry banners;
    public final LootConditionRegistry lootConditions;

    public final ForgeRegistryHelper<Block> blocks;
    public final ForgeRegistryHelper<Item> items;
    public final ForgeRegistryHelper<BlockEntityType<?>> tileEntities;
    public final ForgeRegistryHelper<RecipeSerializer<?>> recipeSerializers;
    public final ForgeRegistryHelper<MenuType<?>> containerTypes;
    public final ForgeRegistryHelper<EntityType<?>> entityTypes;
    public final ForgeRegistryHelper<Motive> paintings;
    public final ForgeRegistryHelper<MobEffect> effects;
    public final ForgeRegistryHelper<Potion> potions;
    public final ForgeRegistryHelper<Enchantment> enchantments;
    public final ForgeRegistryHelper<GlobalLootModifierSerializer<?>> lootModifiers;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RegistryHelper (String modid, Logger logger) {

        this.modid = modid;

        this.commands = new CommandRegistry(logger);
        this.trades = new TradeRegistry(logger);
        this.recipeTypes = new RecipeTypeRegistry(modid, logger);
        this.ingredients = new IngredientRegistry(modid, logger);
        this.registries = new ForgeRegistryRegistryHelper(modid, logger);
        this.lootConditions = new LootConditionRegistry(modid, logger);

        this.blocks = new ForgeRegistryHelper<>(logger, modid, Block.class);
        this.items = new ForgeRegistryHelper<>(logger, modid, Item.class);
        this.tileEntities = new ForgeRegistryHelper(logger, modid, BlockEntityType.class);
        this.recipeSerializers = new ForgeRegistryHelper(logger, modid, RecipeSerializer.class);
        this.containerTypes = new ForgeRegistryHelper(logger, modid, MenuType.class);
        this.entityTypes = new ForgeRegistryHelper(logger, modid, EntityType.class);
        this.paintings = new ForgeRegistryHelper<>(logger, modid, Motive.class);
        this.effects = new ForgeRegistryHelper<>(logger, modid, MobEffect.class);
        this.potions = new ForgeRegistryHelper<>(logger, modid, Potion.class);
        this.enchantments = new ForgeRegistryHelper<>(logger, modid, Enchantment.class);
        this.lootModifiers = new ForgeRegistryHelper(logger, modid, GlobalLootModifierSerializer.class);
        this.banners = new BannerRegistry(modid, logger, this.items);

        this.blocks.addRegisterListener(this::generateBlockItem);
        // TODO Mobs
        // TODO Spawn Eggs
    }

    public RegistryHelper withItemGroup (CreativeModeTab group) {

        this.items.addRegisterListener( (registry, item) -> item.category = group);
        return this;
    }

    public void initialize (IEventBus modBus) {

        this.commands.initialize(modBus);
        this.trades.initialize(modBus);
        this.recipeTypes.initialize(modBus);
        this.ingredients.initialize(modBus);
        this.registries.initialize(modBus);
        this.lootConditions.initialize(modBus);

        this.blocks.initialize(modBus);
        this.items.initialize(modBus);
        this.tileEntities.initialize(modBus);
        this.recipeSerializers.initialize(modBus);
        this.containerTypes.initialize(modBus);
        this.entityTypes.initialize(modBus);
        this.paintings.initialize(modBus);
        this.effects.initialize(modBus);
        this.potions.initialize(modBus);
        this.enchantments.initialize(modBus);
        this.lootModifiers.initialize(modBus);
    }

    private void generateBlockItem (ForgeRegistryHelper<Block> registry, Block block) {

        final Item.Properties itemProps = block instanceof IBookshelfBlock ? ((IBookshelfBlock) block).getItemBlockProperties() : new Item.Properties();

        if (itemProps != null) {

            final Item item = new BlockItem(block, itemProps);
            item.setRegistryName(block.getRegistryName());
            this.items.register(item);
        }
    }
}
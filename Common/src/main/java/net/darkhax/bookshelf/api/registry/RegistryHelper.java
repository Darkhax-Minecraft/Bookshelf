package net.darkhax.bookshelf.api.registry;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.impl.registry.RegistryEntries;
import net.darkhax.bookshelf.mixin.item.AccessorItem;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public abstract class RegistryHelper {

    public final String ownerId;

    public final IRegistryEntries<Block> blocks;
    public final IRegistryEntries<Item> items;
    public final IRegistryEntries<Enchantment> enchantments;
    public final IRegistryEntries<Motive> paintings;
    public final IRegistryEntries<MobEffect> mobEffects;
    public final IRegistryEntries<Attribute> attributes;
    public final IRegistryEntries<VillagerProfession> villagerProfessions;

    public final IRegistryEntries<PreparableReloadListener> serverReloadListeners;
    public final IRegistryEntries<PreparableReloadListener> clientReloadListeners;

    public final IRegistryEntries<ICommandBuilder> commands;
    public final VillagerTradeEntries trades;

    protected RegistryHelper(String ownerId) {

        this.ownerId = ownerId;

        this.blocks = new RegistryEntries<>(ownerId);
        this.items = new RegistryEntries<>(ownerId);
        this.enchantments = new RegistryEntries<>(ownerId);
        this.paintings = new RegistryEntries<>(ownerId);
        this.mobEffects = new RegistryEntries<>(ownerId);
        this.attributes = new RegistryEntries<>(ownerId);
        this.villagerProfessions = new RegistryEntries<>(ownerId);

        this.serverReloadListeners = new RegistryEntries<>(ownerId);
        this.clientReloadListeners = new RegistryEntries<>(ownerId);

        this.commands = new RegistryEntries<>(ownerId);
        this.trades = new VillagerTradeEntries();
    }

    public RegistryHelper withCreativeTab(CreativeModeTab tab) {

        this.items.addInsertListener((id, item) -> ((AccessorItem) item).bookshelf$setCreativeTab(tab));
        return this;
    }

    public abstract ICreativeTabBuilder<?> createTabBuilder(String tabId);

    public abstract void init();
}
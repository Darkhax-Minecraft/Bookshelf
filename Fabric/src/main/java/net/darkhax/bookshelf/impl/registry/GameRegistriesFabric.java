package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class GameRegistriesFabric extends GameRegistriesVanilla {

    @Override
    public void loadContent(RegistryDataProvider content) {

        this.consumeVanillaRegistry(content.blocks, Registry.BLOCK);
        this.consumeVanillaRegistry(content.fluids, Registry.FLUID);
        this.consumeVanillaRegistry(content.items, Registry.ITEM);
        this.consumeVanillaRegistry(content.mobEffects, Registry.MOB_EFFECT);
        this.consumeVanillaRegistry(content.sounds, Registry.SOUND_EVENT);
        this.consumeVanillaRegistry(content.potions, Registry.POTION);
        this.consumeVanillaRegistry(content.enchantments, Registry.ENCHANTMENT);
        this.consumeVanillaRegistry(content.entities, Registry.ENTITY_TYPE);
        this.consumeVanillaRegistry(content.blockEntities, Registry.BLOCK_ENTITY_TYPE);
        this.consumeVanillaRegistry(content.particleTypes, Registry.PARTICLE_TYPE);
        this.consumeVanillaRegistry(content.menus, Registry.MENU);
        this.consumeVanillaRegistry(content.recipeSerializers, Registry.RECIPE_SERIALIZER);
        this.consumeVanillaRegistry(content.paintings, Registry.MOTIVE);
        this.consumeVanillaRegistry(content.attributes, Registry.ATTRIBUTE);
        this.consumeVanillaRegistry(content.stats, Registry.STAT_TYPE);
        this.consumeVanillaRegistry(content.villagerProfessions, Registry.VILLAGER_PROFESSION);

        this.consumeRegistry(content.commandArguments, (id, value) -> ArgumentTypes.register(id.toString(), value.getA(), value.getB()));

        CommandRegistrationCallback.EVENT.register((dispatcher, isDedicated) -> {

            content.commands.build((id, value) -> value.build(dispatcher, isDedicated));
        });
    }

    private <T> void consumeVanillaRegistry(IRegistryEntries<T> toRegister, Registry<T> registry) {

        toRegister.build((id, value) -> Registry.register(registry, id, value));
    }

    private <T> void consumeRegistry(IRegistryEntries<T> toRegister, BiConsumer<ResourceLocation, T> func) {

        toRegister.build(func);
    }
}

package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class LootConditionRegistry {

    private final String ownerId;
    private final Logger logger;

    private final Map<ResourceLocation, LootConditionType> lootConditions;

    public LootConditionRegistry (String ownerId, Logger logger) {

        this.ownerId = ownerId;
        this.logger = logger;
        this.lootConditions = new HashMap<>();
    }

    public void initialize (IEventBus bus) {

        if (!this.lootConditions.isEmpty()) {

            bus.addListener(this::registerCommands);
        }
    }

    public LootConditionType register (ILootSerializer<? extends ILootCondition> serializer, String id) {

        final LootConditionType conditionType = new LootConditionType(serializer);
        this.lootConditions.put(new ResourceLocation(this.ownerId, id), conditionType);
        return conditionType;
    }

    private void registerCommands (FMLCommonSetupEvent event) {

        if (!this.lootConditions.isEmpty()) {

            this.logger.info("Registering {} loot conditions.", this.lootConditions.size());

            for (final Entry<ResourceLocation, LootConditionType> entry : this.lootConditions.entrySet()) {

                // No Forge registry for this yet.
                Registry.register(Registry.LOOT_CONDITION_TYPE, entry.getKey(), entry.getValue());
                this.logger.debug("Registered Loot Condition \"{}\" with type {}.", entry.getKey(), entry.getValue().getClass().getName());
            }
        }
    }
}
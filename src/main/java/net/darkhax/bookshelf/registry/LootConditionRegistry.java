package net.darkhax.bookshelf.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class LootConditionRegistry {

    private final String ownerId;
    private final Logger logger;

    private final Map<ResourceLocation, LootItemConditionType> lootConditions;

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

    public LootItemConditionType register (Serializer<? extends LootItemCondition> serializer, String id) {

        final LootItemConditionType conditionType = new LootItemConditionType(serializer);
        this.lootConditions.put(new ResourceLocation(this.ownerId, id), conditionType);
        return conditionType;
    }

    private void registerCommands (FMLCommonSetupEvent event) {

        if (!this.lootConditions.isEmpty()) {

            this.logger.info("Registering {} loot conditions.", this.lootConditions.size());

            for (final Entry<ResourceLocation, LootItemConditionType> entry : this.lootConditions.entrySet()) {

                // No Forge registry for this yet.
                Registry.register(Registry.LOOT_CONDITION_TYPE, entry.getKey(), entry.getValue());
                this.logger.debug("Registered Loot Condition \"{}\" with type {}.", entry.getKey(), entry.getValue().getClass().getName());
            }
        }
    }
}
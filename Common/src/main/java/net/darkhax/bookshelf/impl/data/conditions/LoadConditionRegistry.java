package net.darkhax.bookshelf.impl.data.conditions;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

/**
 * This condition is only met when all specified registry entries exist in the game registry.
 *
 * @param <T> The type of registry item tested.
 */
public class LoadConditionRegistry<T> implements ILoadConditionSerializer<ILoadCondition> {

    public static final LoadConditionRegistry<Block> BLOCK = new LoadConditionRegistry<>(Services.REGISTRIES.blocks());
    public static final LoadConditionRegistry<Item> ITEM = new LoadConditionRegistry<>(Services.REGISTRIES.items());
    public static final LoadConditionRegistry<Enchantment> ENCHANTMENT = new LoadConditionRegistry<>(Services.REGISTRIES.enchantments());
    public static final LoadConditionRegistry<Motive> PAINTING = new LoadConditionRegistry<>(Services.REGISTRIES.paintings());
    public static final LoadConditionRegistry<MobEffect> MOB_EFFECT = new LoadConditionRegistry<>(Services.REGISTRIES.mobEffects());
    public static final LoadConditionRegistry<Potion> POTION = new LoadConditionRegistry<>(Services.REGISTRIES.potions());
    public static final LoadConditionRegistry<Attribute> ATTRIBUTE = new LoadConditionRegistry<>(Services.REGISTRIES.attributes());
    public static final LoadConditionRegistry<EntityType<?>> ENTITY_TYPE = new LoadConditionRegistry<>(Services.REGISTRIES.entities());
    public static final LoadConditionRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new LoadConditionRegistry<>(Services.REGISTRIES.blockEntities());

    private final IRegistryReader<T> registry;

    public LoadConditionRegistry(IRegistryReader<T> registry) {

        this.registry = registry;
    }

    @Override
    public ILoadCondition fromJson(JsonObject json) {

        final List<ResourceLocation> ids = Serializers.RESOURCE_LOCATION.fromJSONList(json, "values");

        return () -> {

            for (ResourceLocation regId : ids) {

                if (this.registry.get(regId) == null) {

                    return false;
                }
            }

            return true;
        };
    }
}
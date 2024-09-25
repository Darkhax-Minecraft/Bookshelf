package net.darkhax.bookshelf.common.impl.data.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.conditions.ConditionType;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class RegistryContains<T> implements ILoadCondition {

    public static final ResourceLocation BLOCK = Constants.id("block_exists");
    public static final ResourceLocation ITEM = Constants.id("item_exists");
    public static final ResourceLocation ENTITY = Constants.id("entity_exists");
    public static final ResourceLocation BLOCK_ENTITY = Constants.id("block_entity_exists");

    private final Registry<T> registry;
    private final Set<ResourceLocation> requiredIds;
    private final CachedSupplier<ConditionType> type;


    public static <RT> MapCodec<RegistryContains<RT>> of(ResourceLocation typeId, Registry<RT> registry) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                MapCodecs.RESOURCE_LOCATION.getSet("values", RegistryContains::getRequiredEntries)
        ).apply(instance, requiredEntries -> new RegistryContains<>(typeId, registry, requiredEntries)));
    }

    private RegistryContains(ResourceLocation typeId, Registry<T> registry, Set<ResourceLocation> requiredIds) {
        this.registry = registry;
        this.requiredIds = requiredIds;
        this.type = CachedSupplier.cache(() -> LoadConditions.getType(typeId));
    }

    @Override
    public boolean allowLoading() {
        for (ResourceLocation id : this.requiredIds) {
            if (!this.registry.containsKey(id)) {
                return false;
            }
        }
        return true;
    }

    public Set<ResourceLocation> getRequiredEntries() {
        return this.requiredIds;
    }

    @Override
    public ConditionType getType() {
        return this.type.get();
    }
}
package net.darkhax.bookshelf.api.data.conditions.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Supplier;

public class SpecificRegistryContains<T> implements ILoadCondition {

    public static <RT> LoadConditions.ConditionType of(String key, Registry<RT> registry) {

        final ResourceLocation typeId = new ResourceLocation(Constants.MOD_ID, key);
        final Codec<SpecificRegistryContains<RT>> codec = RecordCodecBuilder.create(instance -> instance.group(
                BookshelfCodecs.RESOURCE_LOCATION.getSet("values", SpecificRegistryContains::getRequiredEntries)
        ).apply(instance, requiredEntries -> new SpecificRegistryContains<>(registry, requiredEntries, () -> LoadConditions.getType(typeId))));
        return LoadConditions.register(typeId, codec);
    }

    private final Registry<T> registry;
    private final Set<ResourceLocation> requiredIds;
    private final Supplier<LoadConditions.ConditionType> getType;

    private SpecificRegistryContains(Registry<T> registry, Set<ResourceLocation> requiredIds, Supplier<LoadConditions.ConditionType> getType) {

        this.registry = registry;
        this.requiredIds = requiredIds;
        this.getType = getType;
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
    public LoadConditions.ConditionType getType() {

        return this.getType.get();
    }
}
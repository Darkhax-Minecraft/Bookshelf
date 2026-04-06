package net.darkhax.bookshelf.common.impl.data.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.conditions.ConditionType;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class RegistryContains<T> implements ILoadCondition {

    public static final Identifier BLOCK = BookshelfMod.id("block_exists");
    public static final Identifier ITEM = BookshelfMod.id("item_exists");
    public static final Identifier ENTITY = BookshelfMod.id("entity_exists");
    public static final Identifier BLOCK_ENTITY = BookshelfMod.id("block_entity_exists");

    private final Registry<T> registry;
    private final Set<Identifier> requiredIds;
    private final CachedSupplier<ConditionType> type;


    public static <RT> MapCodec<RegistryContains<RT>> of(Identifier typeId, Registry<RT> registry) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                MapCodecs.RESOURCE_LOCATION.setCodec("values", RegistryContains::getRequiredEntries)
        ).apply(instance, requiredEntries -> new RegistryContains<>(typeId, registry, requiredEntries)));
    }

    private RegistryContains(Identifier typeId, Registry<T> registry, Set<Identifier> requiredIds) {
        this.registry = registry;
        this.requiredIds = requiredIds;
        this.type = CachedSupplier.cache(() -> LoadConditions.getType(typeId));
    }

    @Override
    public boolean allowLoading() {
        for (Identifier id : this.requiredIds) {
            if (!this.registry.containsKey(id)) {
                return false;
            }
        }
        return true;
    }

    public Set<Identifier> getRequiredEntries() {
        return this.requiredIds;
    }

    @Override
    public ConditionType getType() {
        return this.type.get();
    }
}
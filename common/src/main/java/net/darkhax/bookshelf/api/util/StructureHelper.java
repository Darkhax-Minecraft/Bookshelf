package net.darkhax.bookshelf.api.util;

import com.mojang.datafixers.util.Pair;
import net.darkhax.bookshelf.mixin.accessors.world.AccessorStructureTemplatePool;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;

public class StructureHelper {

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"));

    public static StructurePoolElement singleElementFromNBT(RegistryAccess registryAccess, String nbtPath) {

        return poolElementFromNBT(registryAccess, EMPTY_PROCESSOR_LIST_KEY, nbtPath, StructureTemplatePool.Projection.RIGID, false);
    }

    public static StructurePoolElement singleElementFromNBT(RegistryAccess registryAccess, ResourceKey<StructureProcessorList> processorId, String nbtPath) {

        return poolElementFromNBT(registryAccess, processorId, nbtPath, StructureTemplatePool.Projection.RIGID, false);
    }

    public static StructurePoolElement singleElementFromNBT(RegistryAccess registryAccess, ResourceKey<StructureProcessorList> processorId, String nbtPath, StructureTemplatePool.Projection projection) {

        return poolElementFromNBT(registryAccess, processorId, nbtPath, projection, false);
    }

    public static StructurePoolElement legacyElementFromNBT(RegistryAccess registryAccess, String nbtPath) {

        return poolElementFromNBT(registryAccess, EMPTY_PROCESSOR_LIST_KEY, nbtPath, StructureTemplatePool.Projection.RIGID, true);
    }

    public static StructurePoolElement legacyElementFromNBT(RegistryAccess registryAccess, ResourceKey<StructureProcessorList> processorId, String nbtPath) {

        return poolElementFromNBT(registryAccess, processorId, nbtPath, StructureTemplatePool.Projection.RIGID, true);
    }

    public static StructurePoolElement legacyElementFromNBT(RegistryAccess registryAccess, ResourceKey<StructureProcessorList> processorId, String nbtPath, StructureTemplatePool.Projection projection) {

        return poolElementFromNBT(registryAccess, processorId, nbtPath, projection, true);
    }

    public static StructurePoolElement poolElementFromNBT(RegistryAccess registryAccess, ResourceKey<StructureProcessorList> processorId, String nbtPath, StructureTemplatePool.Projection projection, boolean useLegacy) {

        final Holder<StructureProcessorList> processorList = registryAccess.registry(Registries.PROCESSOR_LIST).orElseThrow().getHolderOrThrow(processorId);
        return useLegacy ? StructurePoolElement.legacy(nbtPath, processorList).apply(projection) : StructurePoolElement.single(nbtPath, processorList).apply(projection);
    }

    public static void insertPoolElement(StructureTemplatePool pool, StructurePoolElement element, int weight) {

        if (pool instanceof AccessorStructureTemplatePool poolAccessor) {

            // For some reason Mojang decided to require
            for (int i = 0; i < weight; i++) {
                poolAccessor.bookshelf$getTemplates().add(element);
            }

            // The rawTemplates array is sometimes immutable, so we may need to
            // copy it over to a new array before inserting the element pair.
            if (poolAccessor.bookshelf$getRawTemplates().getClass() != ArrayList.class) {
                poolAccessor.bookshelf$setRawTemplates(new ArrayList<>(poolAccessor.bookshelf$getRawTemplates()));
            }

            poolAccessor.bookshelf$getRawTemplates().add(new Pair<>(element, weight));
        }
    }
}
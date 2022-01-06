package net.darkhax.bookshelf.impl.resources;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Fabric requires modded reload listeners to implement their IdentifiableResourceReloadListener interface. This
 * implementation simply wraps a vanilla compliant reload listener to make it compliant with the Fabric API.
 */
public record WrappedReloadListener(ResourceLocation id,
                                    PreparableReloadListener reloadListener) implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {

        return this.id;
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {

        return reloadListener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
    }
}
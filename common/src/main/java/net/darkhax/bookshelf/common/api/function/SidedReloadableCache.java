package net.darkhax.bookshelf.common.api.function;

import net.darkhax.bookshelf.common.mixin.access.level.AccessorRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SidedReloadableCache<T> implements Function<Level, T> {

    private final ReloadableCache<T> client;
    private final ReloadableCache<T> server;

    public SidedReloadableCache(ReloadableCache<T> client, ReloadableCache<T> server) {
        this.client = client;
        this.server = server;
    }

    public ReloadableCache<T> getCache(Level level) {
        return level.isClientSide ? client : server;
    }

    @Nullable
    @Override
    public T apply(Level level) {
        return getCache(level).apply(level);
    }

    public void invalidate(Level level) {
        getCache(level).invalidate();
    }

    public boolean isCached(Level level) {
        return getCache(level).isCached();
    }

    public void apply(Level level, Consumer<T> consumer) {
        getCache(level).apply(level, consumer);
    }

    public void ifPresent(Level level, Consumer<T> consumer) {
        getCache(level).ifPresent(level, consumer);
    }

    @Nullable
    public <R> R map(Level level, Function<T, R> mapper) {
        return getCache(level).map(level, mapper);
    }

    public static <T> SidedReloadableCache<T> of(Function<Level, T> cacheFunc) {
        return new SidedReloadableCache<>(new ReloadableCache<>(cacheFunc), new ReloadableCache<>(cacheFunc));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> SidedReloadableCache<Map<ResourceLocation, RecipeHolder<T>>> recipes(Supplier<RecipeType<T>> type) {
        return of(level -> {
            final Map<ResourceLocation, RecipeHolder<T>> byId = new HashMap<>();
            if (level.getRecipeManager() instanceof AccessorRecipeManager accessor) {
                final Collection<RecipeHolder<?>> recipes = accessor.bookshelf$byTypeMap().get(type.get());
                recipes.forEach(entry -> byId.put(entry.id(), (RecipeHolder<T>) entry));
            }
            return byId;
        });
    }
}
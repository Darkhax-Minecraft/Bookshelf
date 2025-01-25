package net.darkhax.bookshelf.common.api.function;

import net.darkhax.bookshelf.common.impl.Constants;
import net.darkhax.bookshelf.common.mixin.access.level.AccessorRecipeManager;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A cached value that is lazily loaded and will be invalidated automatically after the game has been reloaded.
 *
 * @param <T> The type of the cached value.
 */
public class ReloadableCache<T> implements Function<Level, T> {

    /**
     * A reloadable cache that will always return null. Not all empty instances will match this instance.
     */
    @SuppressWarnings("rawtypes")
    public static final ReloadableCache EMPTY = ReloadableCache.of(level -> null);

    /**
     * An internal function that is responsible for producing the value to cache.
     */
    private final Function<Level, T> delegate;

    /**
     * A flag that tracks if a value has been cached.
     */
    private boolean cached = false;

    private int revision = 0;

    /**
     * The value that is currently cached.
     */
    @Nullable
    private T cachedValue;

    protected ReloadableCache(Function<Level, T> delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public T apply(Level level) {
        if (!this.isCached() || this.revision != (level.isClientSide ? Constants.CLIENT_REVISION : Constants.SERVER_REVISION)) {
            this.cachedValue = this.delegate.apply(level);
            this.revision = (level.isClientSide) ? Constants.CLIENT_REVISION : Constants.SERVER_REVISION;
            this.cached = true;
        }
        return this.cachedValue;
    }

    /**
     * Manually invalidates the cache. This will result in a new value being cached the next time {@link #apply(Level)}
     * is invoked.
     */
    public void invalidate() {
        this.cached = false;
        this.cachedValue = null;
        this.revision = -1;
    }

    /**
     * Checks if the cache has cached a value. This is not a substitute for null checking.
     *
     * @return Has the supplier cached a value.
     */
    public boolean isCached() {
        return this.cached;
    }

    /**
     * Invokes the consumer with the cached value. This will cause a value to be cached if one has not been cached
     * already.
     *
     * @param level    The current game level. This is used to provide context about the current state of the game.
     * @param consumer The consumer to invoke.
     */
    public void apply(Level level, Consumer<T> consumer) {
        consumer.accept(this.apply(level));
    }

    /**
     * Applies a function to the cached value if the value is not null.
     *
     * @param level    The current game level. This is used to provide context about the current state of the game.
     * @param consumer The consumer to invoke.
     */
    public void ifPresent(Level level, Consumer<T> consumer) {
        final T value = this.apply(level);
        if (value != null) {
            consumer.accept(value);
        }
    }

    /**
     * Maps non null cache values to a new value.
     *
     * @param level  The current game level. This is used to provide context about the current state of the game.
     * @param mapper A mapper function to map the cached value to something new. This is only used if the value is not
     *               null.
     * @param <R>    The return type.
     * @return The mapped value or null.
     */
    @Nullable
    public <R> R map(Level level, Function<T, R> mapper) {
        final T value = this.apply(level);
        return value != null ? mapper.apply(value) : null;
    }

    /**
     * Creates a cache of a value that will be recalculated when the game reloads.
     *
     * @param supplier The supplier used to produce the cached value.
     * @param <T>      The type of value held by the cache.
     * @return A cache of a value that will be recalculated when the game reloads.
     */
    public static <T> ReloadableCache<T> of(Supplier<T> supplier) {
        return new ReloadableCache<>(level -> supplier.get());
    }

    /**
     * Creates a cache of a value that will be recalculated when the game reloads.
     *
     * @param delegate A function used to produce the value to cache.
     * @param <T>      The type of value held by the cache.
     * @return A cache of a value that will be recalculated when the game reloads.
     */
    public static <T> ReloadableCache<T> of(Function<Level, T> delegate) {
        return new ReloadableCache<>(delegate);
    }

    /**
     * Creates a cache of a registry value that will be reaquired when the game reloads.
     *
     * @param registry The registry to look up the value in.
     * @param id       The ID of the value to lookup.
     * @param <T>      The type of value held by the cache.
     * @return A cache of a registry value that will be reaquired when the game reloads.
     */
    public static <T> ReloadableCache<T> of(ResourceKey<? extends Registry<T>> registry, ResourceLocation id) {
        return ReloadableCache.of(level -> level.registryAccess().registryOrThrow(registry).get(id));
    }

    /**
     * Creates a cache of recipe entries for a recipe type.
     *
     * @param type The type of recipe.
     * @param <T>  The type of the recipe.
     * @return A map of recipes for the recipe type.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> ReloadableCache<Map<ResourceLocation, RecipeHolder<T>>> of(RecipeType<T> type) {
        return ReloadableCache.of(level -> {
            final Map<ResourceLocation, RecipeHolder<T>> byId = new HashMap<>();
            if (level.getRecipeManager() instanceof AccessorRecipeManager accessor) {
                final Collection<RecipeHolder<?>> recipes = accessor.bookshelf$byTypeMap().get(type);
                recipes.forEach(entry -> byId.put(entry.id(), (RecipeHolder<T>) entry));
            }
            return byId;
        });
    }

    /**
     * Creates a cache of recipe entries for a recipe type.
     *
     * @param type The type of recipe.
     * @param <T>  The type of the recipe.
     * @return A map of recipes for the recipe type.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> ReloadableCache<Map<ResourceLocation, RecipeHolder<T>>> recipes(Supplier<RecipeType<T>> type) {
        return ReloadableCache.of(level -> {
            final Map<ResourceLocation, RecipeHolder<T>> byId = new HashMap<>();
            if (level.getRecipeManager() instanceof AccessorRecipeManager accessor) {
                final Collection<RecipeHolder<?>> recipes = accessor.bookshelf$byTypeMap().get(type.get());
                recipes.forEach(entry -> byId.put(entry.id(), (RecipeHolder<T>) entry));
            }
            return byId;
        });
    }

    /**
     * Creates a cache of an entity instance.
     *
     * @param entityData The data used to create the entity.
     * @return A reloadable entity instance.
     */
    public static ReloadableCache<Entity> entity(CompoundTag entityData) {
        if (entityData == null || !entityData.contains("id", Tag.TAG_STRING)) {
            throw new IllegalStateException("The provided entity data does not contain an entity ID! data=" + entityData);
        }
        return ReloadableCache.of(level -> {
            try {
                return EntityType.loadEntityRecursive(entityData, level, Function.identity());
            }
            catch (Exception e) {
                throw new IllegalStateException("Encountered an error while constructing the target entity.", e);
            }
        });
    }

    /**
     * Creates a cache of a living entity instance.
     *
     * @param entityData The data used to create the entity.
     * @return A reloadable living entity instance.
     */
    public static ReloadableCache<LivingEntity> living(CompoundTag entityData) {
        final ReloadableCache<Entity> entityCache = entity(entityData);
        return ReloadableCache.of(level -> {
            if (entityCache.apply(level) instanceof LivingEntity living) {
                return living;
            }
            throw new IllegalStateException("Constructed entity was not a LivingEntity type. data=" + entityData);
        });
    }
}
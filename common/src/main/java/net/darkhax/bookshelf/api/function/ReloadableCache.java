package net.darkhax.bookshelf.api.function;

import net.darkhax.bookshelf.mixin.accessors.world.AccessorRecipeManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
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
     * An internal function that is responsible for producing the value to cache.
     */
    private final Function<Level, T> delegate;

    /**
     * A weak reference to the recipe manager that was active when the value was last cached. This is an internal
     * implementation detail that is used to detect when the game has been reloaded.
     */
    private WeakReference<RecipeManager> recipeManager = new WeakReference<>(null);

    /**
     * A flag that tracks if a value has been cached.
     */
    private boolean cached = false;

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

        if (!this.isCached() || hasGameReloaded(level)) {

            this.recipeManager = new WeakReference<>(level.getRecipeManager());
            this.cachedValue = this.delegate.apply(level);
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
        this.recipeManager = new WeakReference<>(null);
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
     * Checks if the game has reloaded since the last time the cache was updated.
     *
     * @param level The current game level. This is used to provide context about the current state of the game.
     * @return If the game has reloaded since the last time the cache was updated.
     */
    public boolean hasGameReloaded(Level level) {

        return this.recipeManager.get() != level.getRecipeManager();
    }

    /**
     * Invokes the consumer with the cached value. This will cause a value to be cached if one has not been cached
     * alread.
     *
     * @param level    The current game level. This is used to provide context about the current state of the game.
     * @param consumer The consumer to invoke.
     */
    public void apply(Level level, Consumer<T> consumer) {

        consumer.accept(this.apply(level));
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
     * @param registry The registry to lookup the value in.
     * @param id       The ID of the value to lookup.
     * @param <T>      The type of value held by the cache.
     * @return A cache of a registry value that will be reaquired when the game reloads.
     */
    public static <T> ReloadableCache<T> of(ResourceKey<? extends Registry<T>> registry, ResourceLocation id) {

        return ReloadableCache.of(level -> level.registryAccess().registryOrThrow(registry).get(id));
    }

    /**
     * Creates a cache of a recipe value that will be reaquired when the game reloads.
     *
     * @param type The type of recipe to lookup.
     * @param id   The ID of the value to lookup.
     * @param <T>  The type of value held by the cache.
     * @return A cache of a registry value that will be reaquired when the game reloads.
     */
    public static <T> ReloadableCache<T> of(RecipeType<? extends Recipe<?>> type, ResourceLocation id) {
        
        return ReloadableCache.of(level -> {

            if (level.getRecipeManager() instanceof AccessorRecipeManager accessor) {

                return (T) accessor.bookshelf$getTypeMap(type).get(id);
            }

            return null;
        });
    }
}
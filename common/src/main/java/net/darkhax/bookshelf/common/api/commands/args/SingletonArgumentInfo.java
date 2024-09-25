package net.darkhax.bookshelf.common.api.commands.args;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * An argument info type that will always resolve to the same singleton instance. This is beneficial when the values are
 * already known by the client in advance, or may even be only known by the client.
 *
 * @param <T> The argument type.
 */
public final class SingletonArgumentInfo<T extends ArgumentType<?>> implements ArgumentTypeInfo<T, SingletonArgumentInfo.Template<T>> {

    /**
     * Creates argument info for a given argument instance.
     *
     * @param argSupplier A supplier that resolves the singleton instance. This supplier should always return the same
     *                    instance!
     * @param <T>         The argument type.
     * @return The argument info for the singleton.
     */
    public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> of(Supplier<T> argSupplier) {
        return new SingletonArgumentInfo<>(argSupplier);
    }

    private final CachedSupplier<Template<T>> templateSupplier;

    private SingletonArgumentInfo(Supplier<T> singletonSupplier) {
        this.templateSupplier = CachedSupplier.cache(() -> new Template<>(singletonSupplier, this));
    }

    @Override
    public void serializeToNetwork(@NotNull Template<T> tTemplate, @NotNull FriendlyByteBuf friendlyByteBuf) {
        // NO-OP
    }

    @NotNull
    @Override
    public Template<T> deserializeFromNetwork(@NotNull FriendlyByteBuf buffer) {
        return this.templateSupplier.get();
    }

    @Override
    public void serializeToJson(@NotNull Template<T> tTemplate, @NotNull JsonObject jsonObject) {
        // NO-OP
    }

    @NotNull
    @Override
    public Template<T> unpack(@NotNull T template) {
        return this.templateSupplier.get();
    }

    /**
     * A template that holds a cached argument singleton.
     *
     * @param <T> The argument type.
     */
    public static class Template<T extends ArgumentType<?>> implements ArgumentTypeInfo.Template<T> {

        private final ArgumentTypeInfo<T, ?> info;
        private final Supplier<T> singletonSupplier;

        protected Template(Supplier<T> supplier, ArgumentTypeInfo<T, ?> info) {
            this.singletonSupplier = supplier;
            this.info = info;
        }

        @NotNull
        @Override
        public T instantiate(@NotNull CommandBuildContext ctx) {
            return this.singletonSupplier.get();
        }

        @NotNull
        @Override
        public ArgumentTypeInfo<T, ?> type() {
            return this.info;
        }
    }
}
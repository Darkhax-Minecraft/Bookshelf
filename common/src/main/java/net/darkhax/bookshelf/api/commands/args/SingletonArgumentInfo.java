package net.darkhax.bookshelf.api.commands.args;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public final class SingletonArgumentInfo<T extends ArgumentType<?>> implements ArgumentTypeInfo<T, SingletonArgumentInfo.Template<T>> {

    public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> of(T argSupplier) {

        return new SingletonArgumentInfo<>(() -> argSupplier);
    }

    public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> of(Supplier<T> argSupplier) {

        return new SingletonArgumentInfo<>(argSupplier);
    }

    private final CachedSupplier<Template<T>> templateSupplier;

    private SingletonArgumentInfo(Supplier<T> singletonSupplier) {

        this.templateSupplier = CachedSupplier.cache(() -> new Template<>(singletonSupplier, this));
    }

    @Override
    public void serializeToNetwork(Template<T> tTemplate, FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public Template<T> deserializeFromNetwork(FriendlyByteBuf buffer) {

        return this.templateSupplier.get();
    }

    @Override
    public void serializeToJson(Template<T> tTemplate, JsonObject jsonObject) {

    }

    @Override
    public Template<T> unpack(T template) {

        return this.templateSupplier.get();
    }

    public static class Template<T extends ArgumentType<?>> implements ArgumentTypeInfo.Template<T> {

        private final ArgumentTypeInfo<T, ?> info;
        private final Supplier<T> singletonSupplier;

        public Template(Supplier<T> supplier, ArgumentTypeInfo<T, ?> info) {

            this.singletonSupplier = supplier;
            this.info = info;
        }

        @Override
        public T instantiate(CommandBuildContext ctx) {

            return this.singletonSupplier.get();
        }

        @Override
        public ArgumentTypeInfo<T, ?> type() {

            return this.info;
        }
    }
}
package net.darkhax.bookshelf.api.commands.args;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public final class SingletonArgumentSerializer<T extends ArgumentType<?>> implements ArgumentSerializer<T> {

    public static <T extends ArgumentType<?>> SingletonArgumentSerializer<T> of(T argSupplier) {

        return new SingletonArgumentSerializer<>(() -> argSupplier);
    }

    public static <T extends ArgumentType<?>> SingletonArgumentSerializer<T> of(Supplier<T> argSupplier) {

        return new SingletonArgumentSerializer<>(argSupplier);
    }

    private final Supplier<T> singletonSupplier;

    private SingletonArgumentSerializer(Supplier<T> singletonSupplier) {

        this.singletonSupplier = singletonSupplier;
    }

    @Override
    public void serializeToNetwork(T arg, FriendlyByteBuf buffer) {

    }

    @Override
    public T deserializeFromNetwork(FriendlyByteBuf buffer) {

        return this.singletonSupplier.get();
    }

    @Override
    public void serializeToJson(T arg, JsonObject json) {

    }
}
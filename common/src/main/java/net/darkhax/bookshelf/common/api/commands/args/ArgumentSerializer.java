package net.darkhax.bookshelf.common.api.commands.args;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ArgumentSerializer<T extends ArgumentType<?>, V> implements ArgumentTypeInfo<T, ArgumentSerializer.ArgTemplate<T, V>> {

    private final MapCodec<V> codec;
    private final StreamCodec<FriendlyByteBuf, V> stream;
    private final BiFunction<CommandBuildContext, V, T> fromData;
    private final Function<T, V> toData;

    public ArgumentSerializer(MapCodec<V> codec, StreamCodec<FriendlyByteBuf, V> stream, BiFunction<CommandBuildContext, V, T> mapFunc, Function<T, V> toData) {
        this.codec = codec;
        this.stream = stream;
        this.fromData = mapFunc;
        this.toData = toData;
    }

    @Override
    public void serializeToNetwork(ArgTemplate<T, V> template, @NotNull FriendlyByteBuf buf) {
        this.stream.encode(buf, template.data);
    }

    @NotNull
    @Override
    public ArgTemplate<T, V> deserializeFromNetwork(@NotNull FriendlyByteBuf buf) {
        return new ArgTemplate<>(this, this.stream.decode(buf));
    }

    @Override
    public void serializeToJson(@NotNull ArgTemplate<T, V> template, @NotNull JsonObject json) {
        json.add("value", this.codec.codec().encodeStart(JsonOps.INSTANCE, template.data).getOrThrow());
    }

    @NotNull
    @Override
    public ArgTemplate<T, V> unpack(@NotNull T t) {
        return new ArgTemplate<>(this, this.toData.apply(t));
    }

    public static class ArgTemplate<T extends ArgumentType<?>, V> implements ArgumentTypeInfo.Template<T> {

        private final ArgumentSerializer<T, V> type;
        private final V data;

        protected ArgTemplate(ArgumentSerializer<T, V> type, V data) {
            this.type = type;
            this.data = data;
        }

        @NotNull
        @Override
        public T instantiate(@NotNull CommandBuildContext ctx) {
            return this.type.fromData.apply(ctx, this.data);
        }

        @NotNull
        @Override
        public ArgumentTypeInfo<T, ?> type() {
            return this.type;
        }
    }
}
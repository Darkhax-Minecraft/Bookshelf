package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.mojang.brigadier.arguments.ArgumentType;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CommandArgumentAdapter extends GenericRegistryAdapter<CommandArgumentAdapter.TypeInfo<?>> {

    public CommandArgumentAdapter(RegistrationContext context, BiConsumer<ResourceLocation, Supplier<TypeInfo<?>>> registryFunc) {
        super(context, registryFunc);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void add(String id, Class argumentClass, ArgumentTypeInfo<A, T> info) {
        this.add(id, new TypeInfo<>(argumentClass, info));
    }

    public record TypeInfo<A extends ArgumentType<?>>(Class<? extends ArgumentType<?>> argType, ArgumentTypeInfo<A, ?> typeIfo) {
    }
}
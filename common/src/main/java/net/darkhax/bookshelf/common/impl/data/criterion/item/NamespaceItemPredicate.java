package net.darkhax.bookshelf.common.impl.data.criterion.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public record NamespaceItemPredicate(String namespace) implements ItemSubPredicate {

    public static final Codec<NamespaceItemPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(MapCodecs.STRING.get("namespace", NamespaceItemPredicate::namespace)).apply(instance, NamespaceItemPredicate::new));

    @Override
    public boolean matches(ItemStack stack) {
        return namespace.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace());
    }
}
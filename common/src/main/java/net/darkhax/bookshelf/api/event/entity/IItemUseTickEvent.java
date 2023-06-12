package net.darkhax.bookshelf.api.event.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface IItemUseTickEvent {

    void onUseTick(LivingEntity user, ItemStack stack, AtomicInteger duration);
}
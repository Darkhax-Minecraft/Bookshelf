package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SoundEventAdapter extends GameRegistryAdapter<SoundEvent> {

    public SoundEventAdapter(RegistrationContext context, ResourceKey<Registry<SoundEvent>> registryKey, BiConsumer<ResourceKey<SoundEvent>, Supplier<SoundEvent>> registryFunc) {
        super(context, registryKey, registryFunc);
    }

    public void fixedRange(String id, float range) {
        this.add(id, SoundEvent.createFixedRangeEvent(this.id(id), range));
    }

    public void variableRange(String id) {
        this.add(id, SoundEvent.createVariableRangeEvent(this.id(id)));
    }
}
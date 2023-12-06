package net.darkhax.bookshelf.api.data.conditions.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;

import java.util.Set;

public class ModLoaded implements ILoadCondition {

    public static Codec<ModLoaded> CODEC = RecordCodecBuilder.create(instance -> instance.group(BookshelfCodecs.STRING.getSet("values", ModLoaded::getRequiredMods)).apply(instance, ModLoaded::new));
    private final Set<String> requiredMods;

    private ModLoaded(Set<String> requiredMods) {

        this.requiredMods = requiredMods;
    }

    @Override
    public boolean allowLoading() {

        for (String modId : this.requiredMods) {

            if (!Services.PLATFORM.isModLoaded(modId)) {

                return false;
            }
        }

        return true;
    }

    public Set<String> getRequiredMods() {

        return this.requiredMods;
    }

    @Override
    public LoadConditions.ConditionType getType() {

        return LoadConditions.MOD_LOADED;
    }
}
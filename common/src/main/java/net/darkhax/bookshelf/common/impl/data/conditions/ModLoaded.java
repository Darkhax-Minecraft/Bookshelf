package net.darkhax.bookshelf.common.impl.data.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.conditions.ConditionType;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * This load condition will test that an array of mod IDs are all loaded.
 */
public class ModLoaded implements ILoadCondition {

    public static final ResourceLocation TYPE_ID = Constants.id("mod_loaded");
    public static final CachedSupplier<ConditionType> TYPE = CachedSupplier.cache(() -> LoadConditions.getType(TYPE_ID));
    public static MapCodec<ModLoaded> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(MapCodecs.STRING.getSet("values", ModLoaded::getRequiredMods)).apply(instance, ModLoaded::new));

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
    public ConditionType getType() {
        return TYPE.get();
    }
}
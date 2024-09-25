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

/**
 * This load condition will test the current mod loading platform.
 */
public class OnPlatform implements ILoadCondition {

    public static final ResourceLocation TYPE_ID = Constants.id("on_platform");
    public static final CachedSupplier<ConditionType> TYPE = CachedSupplier.cache(() -> LoadConditions.getType(TYPE_ID));
    public static final MapCodec<OnPlatform> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(MapCodecs.STRING.get("platform", OnPlatform::getRequiredPlatform)).apply(instance, OnPlatform::new));

    private final String requiredPlatform;

    private OnPlatform(String requiredPlatform) {
        this.requiredPlatform = requiredPlatform;
    }

    @Override
    public boolean allowLoading() {
        return Services.PLATFORM.getName().equalsIgnoreCase(this.getRequiredPlatform());
    }

    public String getRequiredPlatform() {
        return this.requiredPlatform;
    }

    @Override
    public ConditionType getType() {
        return TYPE.get();
    }
}

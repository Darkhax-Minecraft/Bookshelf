package net.darkhax.bookshelf.common.impl.data.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.conditions.ConditionType;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * This load condition will test an array of sub-conditions and make sure at least one of them are met.
 */
public class Or implements ILoadCondition {

    public static final ResourceLocation TYPE_ID = Constants.id("or");
    public static final CachedSupplier<ConditionType> TYPE = CachedSupplier.cache(() -> LoadConditions.getType(TYPE_ID));
    public static final MapCodec<Or> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LoadConditions.CODEC_HELPER.getList("conditions", Or::getConditions)).apply(instance, Or::new));

    private final List<ILoadCondition> conditions;

    private Or(List<ILoadCondition> conditions) {
        this.conditions = conditions;
    }

    public List<ILoadCondition> getConditions() {
        return this.conditions;
    }

    @Override
    public boolean allowLoading() {
        for (ILoadCondition condition : this.conditions) {
            if (condition.allowLoading()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionType getType() {
        return TYPE.get();
    }
}
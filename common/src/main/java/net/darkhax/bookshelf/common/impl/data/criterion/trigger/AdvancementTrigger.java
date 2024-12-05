package net.darkhax.bookshelf.common.impl.data.criterion.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class AdvancementTrigger extends SimpleCriterionTrigger<AdvancementTrigger.Instance> {

    public static final AdvancementTrigger TRIGGER = new AdvancementTrigger();
    private static final Codec<AdvancementTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
            MapCodecs.RESOURCE_LOCATION.getSet("advancements", Instance::advancementIds)
    ).apply(instance, Instance::new));

    @Override
    @NotNull
    public Codec<AdvancementTrigger.Instance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer player, AdvancementHolder advancement) {
        this.trigger(player, instance -> instance.advancementIds().contains(advancement.id()));
    }

    public record Instance(Optional<ContextAwarePredicate> player, Set<ResourceLocation> advancementIds) implements SimpleCriterionTrigger.SimpleInstance {

        @Override
        @NotNull
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}

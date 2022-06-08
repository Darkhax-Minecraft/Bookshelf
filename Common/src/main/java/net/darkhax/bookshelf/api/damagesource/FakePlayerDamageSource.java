package net.darkhax.bookshelf.api.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * A damage source that is tied to a fake player. Mobs killed by this damage source type can potentially drop loot even
 * when no player was present.
 */
public class FakePlayerDamageSource extends DamageSource {

    /**
     * A unique namespaced ID that is tied to the fake player damage source. This is used to provide debug info and
     * generate player death messages.
     */
    private final ResourceLocation sourceId;

    public FakePlayerDamageSource(ResourceLocation sourceId) {

        super(sourceId.toString());
        this.sourceId = sourceId;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity deadMob) {

        return Component.translatable("death.attack." + this.sourceId.getNamespace() + "." + this.sourceId.getPath(), deadMob.getDisplayName());
    }

    /**
     * Checks if the corresponding loot roll should forcefully allow player only loot drops to be produced. The default
     * behaviour is to always allow these drops when this damage source is used.
     * <p>
     * When this method returns true the result of {@link net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition#test(LootContext)}
     * will be forcefully true.
     *
     * @param context Additional loot table context that can be used for additional context checks.
     * @return Should the corresponding loot roll be able to allow player only loot drops.
     */
    public boolean shouldDropPlayerLoot(LootContext context) {

        return true;
    }

    /**
     * A helper method that will cause damage to a mob using the source.
     *
     * @param entity The entity to damage.
     * @param damage The amount of damage to do.
     */
    public void causeDamage(LivingEntity entity, float damage) {

        entity.hurt(this, damage);

        // Sets the last tick the mob was hurt by a "player". This is required for EXP to drop.
        entity.setLastHurtByPlayer(null);
    }
}
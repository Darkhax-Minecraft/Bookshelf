package net.darkhax.bookshelf.common.api.entity.villager;

public enum MerchantTier {

    NOVICE(0),
    APPRENTICE(10),
    JOURNEYMAN(70),
    EXPERT(150),
    MASTER(250);

    private final int requiredExp;

    MerchantTier(int requiredExp) {

        this.requiredExp = requiredExp;
    }

    public int getRequiredExp() {
        return this.requiredExp;
    }
}
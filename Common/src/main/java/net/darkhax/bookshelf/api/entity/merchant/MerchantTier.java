package net.darkhax.bookshelf.api.entity.merchant;

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
}

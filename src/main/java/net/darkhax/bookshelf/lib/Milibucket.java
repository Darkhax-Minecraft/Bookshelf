package net.darkhax.bookshelf.lib;

/**
 * Represents common mb amounts.
 */
public enum Milibucket {
    
    NUGGET(16),
    INGOT(144),
    Bottle(333),
    BUCKET(1000),
    BLOCK(1296);
    
    /**
     * The amount of mb which make up this amount.
     */
    public int amount;
    
    /**
     * A simple enumeration used to list how many milibuckets is in a given measurement.
     * 
     * @param amount The amount of milibuckets in the measurement.
     */
    Milibucket(int amount) {
        
        this.amount = amount;
    }
}

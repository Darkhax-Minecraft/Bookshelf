package net.darkhax.bookshelf.util;

public class Tuple {
    
    private Object firstObject;
    private Object secondObject;
    
    /**
     * Creates a new Tuple instance.
     * 
     * @param firstObj: The first object in the pair.
     * @param secondObj: The second object in the pair.
     */
    public Tuple(Object firstObj, Object secondObj) {
    
        this.firstObject = firstObj;
        this.secondObject = secondObj;
    }
    
    /**
     * Retrieves the first object in the Tuple pair.
     * 
     * @return Object: The first object stored in the Tuple instance.
     */
    public Object getFirstObject () {
    
        return this.firstObject;
    }
    
    /**
     * Retrieves the second object in the Tuple pair.
     * 
     * @return Object: The second object stored in the Tuple instance.
     */
    public Object getSecondObject () {
    
        return this.secondObject;
    }
}
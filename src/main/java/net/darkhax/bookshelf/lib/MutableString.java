package net.darkhax.bookshelf.lib;

public class MutableString implements java.io.Serializable, Comparable<String>, CharSequence {
    
    private static final long serialVersionUID = 8345543667917868717L;
    
    /**
     * The String value held by the MutableString.
     */
    private String value;
    
    /**
     * Constructs a MutableString object. This variant on String allows for the instance to be
     * altered, as opposed to Java's immutable String.
     * 
     * @param string: The value to set.
     */
    public MutableString(String string) {
        
        this.value = string;
    }
    
    /**
     * Sets the value of the MutableStrong.
     * 
     * @param string: The value to set.
     */
    public void setValue (String string) {
        
        this.value = string;
    }
    
    /**
     * Retrieves the value of the MutableString.
     * 
     * @return String: The value held by the MutableString.
     */
    public String getValue () {
        
        return this.value;
    }
    
    @Override
    public int length () {
        
        return this.value.length();
    }
    
    @Override
    public char charAt (int index) {
        
        return this.value.charAt(index);
    }
    
    @Override
    public CharSequence subSequence (int start, int end) {
        
        return this.value.subSequence(start, end);
    }
    
    @Override
    public String toString () {
        
        return this.value;
    }
    
    @Override
    public int compareTo (String o) {
        
        return this.value.compareTo(o);
    }
    
    @Override
    public boolean equals (Object obj) {
        
        return this.value.equals(obj);
    }
}
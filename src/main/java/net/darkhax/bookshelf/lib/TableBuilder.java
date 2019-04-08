/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.darkhax.bookshelf.Bookshelf;

public class TableBuilder<T> {
    
    private static final String DIVIDER_COLUMN = "|";
    
    private static final String DIVIDER_ROW = "-";
    
    private static final String LINE_ENDING = DIVIDER_ROW + DIVIDER_ROW + DIVIDER_COLUMN;
    
    /**
     * An internal list of entries, used to make list population much easier.
     */
    private final List<T> entries = new ArrayList<>();
    
    /**
     * A list of names used for the columns of the table.
     */
    private final List<String> columnNames = new ArrayList<>();
    
    /**
     * A list of functions which match up with the columns of the table.
     */
    private final List<Function<? super T, String>> columnFunctions = new ArrayList<>();
    
    /**
     * Defines a new column for the table. This sets the name of the column, and a function
     * which is used to generate row data.
     *
     * @param name The name for the column.
     * @param function The function to apply to the object. Used to generate row data for the
     *        column.
     */
    public void addColumn (String name, Function<? super T, ?> function) {
        
        this.columnNames.add(name);
        this.columnFunctions.add(p -> String.valueOf(function.apply(p)));
    }
    
    /**
     * Gets the longest width amongst all data in a column.
     *
     * @param columnIndex The index of the column.
     * @param entries The entries for the table.
     * @return The maximum width for the column.
     */
    private int getMaxWidth (int columnIndex, Iterable<? extends T> entries) {
        
        int maxWidth = this.columnNames.get(columnIndex).length();
        
        final Function<? super T, String> function = this.columnFunctions.get(columnIndex);
        
        for (final T entry : entries) {
            
            final String data = function.apply(entry);
            maxWidth = Math.max(maxWidth, data.length());
        }
        
        return maxWidth;
    }
    
    /**
     * Pads a string to the left.
     *
     * @param string The string to pad.
     * @param padCharacter The character to pad the string with.
     * @param length The desired length for the string.
     * @return The padded string.
     */
    private String padLeft (String string, String padCharacter, int length) {
        
        while (string.length() < length) {
            
            string = padCharacter + string;
        }
        
        return string;
    }
    
    /**
     * Gets a list of integers which represent the width for each column.
     *
     * @param entries The entries for the column.
     * @return A list of integers which represent the longest width for each column.
     */
    private List<Integer> getColumnWidths (Iterable<? extends T> entries) {
        
        final List<Integer> columnWidths = new ArrayList<>();
        
        for (int columnIndex = 0; columnIndex < this.columnNames.size(); columnIndex++) {
            
            columnWidths.add(this.getMaxWidth(columnIndex, entries));
        }
        
        return columnWidths;
    }
    
    /**
     * Adds an entry to the table.
     *
     * @param entry The entry to add to the table.
     */
    public void addEntry (T entry) {
        
        this.entries.add(entry);
    }
    
    /**
     * Creates the table string using the entries from {@link #entries}.
     *
     * @return A string which represents the table data as a markdown table.
     */
    public String createString () {
        
        return this.createString(this.entries);
    }
    
    public String createString (Iterable<? extends T> entries) {
        
        final List<Integer> widths = this.getColumnWidths(entries);
        final int columnCount = this.columnNames.size();
        
        final StringBuilder builder = new StringBuilder();
        
        // Column Names
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            
            builder.append(DIVIDER_COLUMN);
            final String columnHeader = "%-" + widths.get(columnIndex) + "s";
            builder.append(" " + String.format(columnHeader, this.columnNames.get(columnIndex)) + " ");
        }
        
        builder.append(DIVIDER_COLUMN);
        builder.append(Bookshelf.NEW_LINE);
        
        // Column Seperator
        builder.append(DIVIDER_COLUMN);
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            
            if (columnIndex > 0) {
                
                builder.append(LINE_ENDING);
            }
            
            builder.append(this.padLeft("", DIVIDER_ROW, widths.get(columnIndex)));
        }
        
        builder.append(LINE_ENDING);
        builder.append(Bookshelf.NEW_LINE);
        
        // Column Data
        for (final T entry : entries) {
            
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                
                builder.append(DIVIDER_COLUMN);
                
                final String format = "%-" + widths.get(columnIndex) + "s";
                final Function<? super T, String> function = this.columnFunctions.get(columnIndex);
                
                final String columnText = function.apply(entry);
                builder.append(" " + String.format(format, columnText) + " ");
            }
            
            builder.append(DIVIDER_COLUMN);
            builder.append(Bookshelf.NEW_LINE);
        }
        
        return builder.toString();
    }
    
    /**
     * Gets the list of entries. Can be used to do things like sort the entries before
     * printing.
     *
     * @return The list of all entries.
     */
    public List<T> getEntries () {
        
        return this.entries;
    }
}
/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public final class TextUtils {
    
    /**
     * Applies a font to a text component and all of it's sibling components.
     * 
     * @param text The component to apply the font to.
     * @param font The font to apply.
     * @return The text component with he font applied.
     */
    public static ITextComponent applyFont (ITextComponent text, ResourceLocation font) {
        
        if (text instanceof IFormattableTextComponent) {
            
            ((IFormattableTextComponent) text).setStyle(text.getStyle().withFont(font));
        }
        
        text.getSiblings().forEach(sib -> applyFont(sib, font));
        return text;
    }
    
    /**
     * Joins multiple text components together using a separator.
     * 
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin An array of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static IFormattableTextComponent join (ITextComponent separator, ITextComponent... toJoin) {
        
        return join(separator, Arrays.stream(toJoin).iterator());
    }
    
    /**
     * Joins multiple text components together using a separator.
     * 
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin A collection of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static IFormattableTextComponent join (ITextComponent separator, Collection<ITextComponent> toJoin) {
        
        return join(separator, toJoin.iterator());
    }
    
    /**
     * Joins multiple text components together using a separator.
     * 
     * @param separator The separator text component. This will be added in between entries.
     * @param toJoin An iterator of text components to join.
     * @return A joint text component containing all the input values.
     */
    public static IFormattableTextComponent join (ITextComponent separator, Iterator<ITextComponent> toJoin) {
        
        final StringTextComponent joined = new StringTextComponent("");
        
        while (toJoin.hasNext()) {
            
            joined.append(toJoin.next());
            
            if (toJoin.hasNext()) {
                
                joined.append(separator);
            }
        }
        
        return joined;
    }
}
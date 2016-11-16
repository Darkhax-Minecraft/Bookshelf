/*******************************************************************************************************************
 * Copyright: covers1624
 *
 * License: Lesser General Public License 2.1
 *          https://github.com/TheCBProject/CodeChickenLib/blob/397135f68774e0661d23cb608c893274635d6d6d/LICENSEw
 *            
 * Original: https://github.com/TheCBProject/CodeChickenLib/blob/6d2202b3328e564509371db283a40d4b4d752287/src/main/java/codechicken/lib/render/item/IGLTransform.java
 * 
 * Changes: - Reformatted to Bookshelf's code style and formatting.
 *          - Wrote complete Javadocs.
 *          - Added the IBakedModel as a parameter.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.client.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

/**
 * Implement this on your IBakedModel to be able to render additonal things using GL.
 */
public interface IItemRenderer extends IBakedModel {
    
    /**
     * Handles additional rendering for the ItemStack.
     * 
     * @param item The ItemStack being rendered.
     */
    void renderItem (ItemStack item);
}
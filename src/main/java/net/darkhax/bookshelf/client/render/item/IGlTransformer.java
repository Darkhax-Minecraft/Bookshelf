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
 *          - Renamed class to IGlTransformer
 *******************************************************************************************************************/
package net.darkhax.bookshelf.client.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

/**
 * Implement this on your IBakedModel to be able to apply transformations using GL code.
 */
public interface IGlTransformer extends IBakedModel {
    
    /**
     * Used to apply transformations using GL code.
     *
     * @param transformType The type of transformation that should be done.
     * @param isLeftHand Whether or not this is being done in the left hand.
     */
    void applyTransforms (TransformType transformType, boolean isLeftHand);
}
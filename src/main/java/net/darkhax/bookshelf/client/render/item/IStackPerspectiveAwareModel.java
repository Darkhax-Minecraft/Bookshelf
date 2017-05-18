/*******************************************************************************************************************
 * Copyright: covers1624
 *
 * License: Lesser General Public License 2.1
 *          https://github.com/TheCBProject/CodeChickenLib/blob/397135f68774e0661d23cb608c893274635d6d6d/LICENSEw
 *
 * Original: https://github.com/TheCBProject/CodeChickenLib/blob/6d2202b3328e564509371db283a40d4b4d752287/src/main/java/codechicken/lib/render/item/IStackPerspectiveAwareModel.java
 *
 * Changes: - Reformatted to Bookshelf's code style and formatting.
 *          - Wrote complete Javadocs.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.client.render.item;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;

/**
 * Implement this on your IBakedModel to be able to change it based on perspective.
 */
public interface IStackPerspectiveAwareModel extends IBakedModel {

    /**
     * Handles the perspectives of the model in a way similar to IPerspectiveAwareModel however
     * this also gives you the ItemStack context. Can be used to change model based on how the
     * item is being looked at. IE in hand vs 3rd person vs inventory.
     *
     * @param stack The ItemStack being looked at.
     * @param cameraTransformType The type of perspective that is being used to observe the
     *        item.
     * @return A pair, including the model to use and the matrix transformations to be applied
     *         to it before being rendered. The matrix can be null.
     */
    Pair<? extends IBakedModel, Matrix4f> handlePerspective (ItemStack stack, TransformType cameraTransformType);
}
package net.darkhax.bookshelf.client.event;

import java.awt.Color;

import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This event is deprecated, and does not work. Hopefully the deprecated annotations made that
 * clear lol. I have moved this class to net/darkhax/bookshelf/events. This event will not be
 * fired, and all dependent features will not be called. Added this back because it should have
 * been deprecated in the first place.
 */
@SideOnly(Side.CLIENT)
@Deprecated
public class RenderItemEvent extends Event {

    /**
     * Instance of the item renderer.
     */
    @Deprecated
    private final RenderItem renderer;

    /**
     * The ItemStack being rendered.
     */
    @Deprecated
    private final ItemStack stack;

    /**
     * The model used for the item.
     */
    @Deprecated
    private final IBakedModel model;

    /**
     * Base event constructor. See sub events for more info on this event!
     *
     * @param renderer Instance of the item renderer.
     * @param stack The ItemStack being rendered.
     * @param model The model used for the item.
     */
    @Deprecated
    public RenderItemEvent (RenderItem renderer, ItemStack stack, IBakedModel model) {

        this.renderer = renderer;
        this.stack = stack;
        this.model = model;
    }

    /**
     * Gets the instance of RenderItem.
     *
     * @return The instance of RenderItem.
     */
    @Deprecated
    public RenderItem getRenderer () {

        return this.renderer;
    }

    /**
     * Gets the stack being rendered.
     *
     * @return The stack being rendered.
     */
    @Deprecated
    public ItemStack getItemStack () {

        return this.stack;
    }

    /**
     * Gets the model being used.
     *
     * @return The model being used.
     */
    @Deprecated
    public IBakedModel getModel () {

        return this.model;
    }

    @Deprecated
    @Cancelable
    public static class Allow extends RenderItemEvent {

        /**
         * Event that checks if anything wants to use custom item rendering. Must be canceled
         * if you want to use any of the other sub events. Called before anything else in the
         * item render code.
         *
         * @param renderer Instance of the item renderer.
         * @param stack The ItemStack being rendered.
         * @param model The model used for the item.
         */
        @Deprecated
        public Allow (RenderItem renderer, ItemStack stack, IBakedModel model) {

            super(renderer, stack, model);
        }
    }

    @Deprecated
    public static class Pre extends RenderItemEvent {

        /**
         * This event is fired before the item model is rendered, but after the matrix is
         * pushed, and the item has been translated to the default position.
         *
         * @param renderer Instance of the item renderer.
         * @param stack The ItemStack being rendered.
         * @param model The model used for the item.
         */
        @Deprecated
        public Pre (RenderItem renderer, ItemStack stack, IBakedModel model) {

            super(renderer, stack, model);
        }
    }

    @Deprecated
    public static class Post extends RenderItemEvent {

        /**
         * This event is fired after the item model has been rendered, but not before the matix
         * has been poped.
         *
         * @param renderer Instance of the item renderer.
         * @param stack The ItemStack being rendered.
         * @param model The model used for the item.
         */
        @Deprecated
        public Post (RenderItem renderer, ItemStack stack, IBakedModel model) {

            super(renderer, stack, model);
        }
    }

    @Deprecated
    @Cancelable
    public static class Glint extends RenderItemEvent {

        /**
         * The color to use when rendering the glint effect.
         */
        @Deprecated
        private Color color = new Color(-8372020);

        /**
         * Whether or not the color requires a reset. The default color is purple, this sets it
         * to black, allowing new colors to be applied and blended.
         */
        @Deprecated
        private boolean requiresReset = true;

        /**
         * The texture to use when rendering the glint effect.
         */
        @Deprecated
        public ResourceLocation texture = RenderUtils.RES_ITEM_GLINT;

        /**
         * This event is fired in the middle of item rendering. It allows a new glint effect to
         * be applied, which will replace the vanilla one. This event must be canceled to
         * enable custom rendering effects.
         *
         * @param renderer Instance of the item renderer.
         * @param stack The ItemStack being rendered.
         * @param model The model used for the item.
         */
        @Deprecated
        public Glint (RenderItem renderer, ItemStack stack, IBakedModel model) {

            super(renderer, stack, model);
        }

        /**
         * Adds color to the glint effect. Will remove the default purple modifier!
         *
         * @param red The red amount to add.
         * @param green The green amount to add.
         * @param blue The blue amount to add.
         */
        @Deprecated
        public void addColor (float red, float green, float blue) {

            if (this.requiresReset) {

                this.requiresReset = false;
                this.color = new Color(0, 0, 0);
            }

            this.color = new Color(this.getColor().getRed() + red, this.getColor().getGreen() + green, this.getColor().getBlue() + blue);
        }

        /**
         * Gets the color to render.
         *
         * @return The color to use when rendering the glint effect.
         */
        @Deprecated
        public Color getColor () {

            return this.color;
        }
    }
}
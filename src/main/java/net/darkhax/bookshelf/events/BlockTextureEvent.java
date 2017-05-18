package net.darkhax.bookshelf.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Cancelable
@SideOnly(Side.CLIENT)
public class BlockTextureEvent extends Event {

    /**
     * The state to get the texture for.
     */
    private final IBlockState state;

    /**
     * The texture for the sprite.
     */
    private TextureAtlasSprite sprite;

    /**
     * Constructor.
     *
     * @param state The stage to fire the event for.
     */
    public BlockTextureEvent (IBlockState state) {

        this.state = state;
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public IBlockState getState () {

        return this.state;
    }

    /**
     * Gets the sprite for the event. This will be null unless set.
     *
     * @return The sprite.
     */
    public TextureAtlasSprite getSprite () {

        return this.sprite;
    }

    /**
     * Sets the sprite for the event.
     *
     * @param sprite The sprite for the event.
     */
    public void setSprite (TextureAtlasSprite sprite) {

        this.sprite = sprite;
    }
}
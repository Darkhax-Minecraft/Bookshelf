package net.darkhax.bookshelf.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockTextureEvent extends Event {

    private final IBlockState state;

    private TextureAtlasSprite sprite;

    public BlockTextureEvent (IBlockState state) {

        this.state = state;
    }

    public IBlockState getState () {

        return this.state;
    }

    public TextureAtlasSprite getSprite () {

        return this.sprite;
    }

    public void setSprite (TextureAtlasSprite sprite) {

        this.sprite = sprite;
    }
}

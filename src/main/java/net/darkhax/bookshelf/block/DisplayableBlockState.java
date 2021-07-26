package net.darkhax.bookshelf.block;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.darkhax.bookshelf.serialization.ISerializer;
import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DisplayableBlockState {

    public static ISerializer<DisplayableBlockState> SERIALIZER = new Serializer();

    private BlockState state;
    private Optional<Vector3f> scale;
    private Optional<Vector3f> offset;
    private boolean renderFluid;

    public DisplayableBlockState (BlockState state) {

        this(state, Optional.empty(), Optional.empty(), true);
    }

    public DisplayableBlockState (BlockState state, Optional<Vector3f> scale, Optional<Vector3f> offset, boolean renderFluid) {

        this.state = state;
        this.scale = scale;
        this.offset = offset;
        this.renderFluid = renderFluid;
    }

    public BlockState getState () {

        return this.state;
    }

    public void setState (BlockState state) {

        this.state = state;
    }

    public Optional<Vector3f> getScale () {

        return this.scale;
    }

    public void setScale (Optional<Vector3f> scale) {

        this.scale = scale;
    }

    public Optional<Vector3f> getOffset () {

        return this.offset;
    }

    public void setOffset (Optional<Vector3f> offset) {

        this.offset = offset;
    }

    public boolean isRenderFluid () {

        return this.renderFluid;
    }

    public void setRenderFluid (boolean renderFluid) {

        this.renderFluid = renderFluid;
    }

    @OnlyIn(Dist.CLIENT)
    public void render (World world, BlockPos pos, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, Direction... preferredSides) {

        matrix.pushPose();
        this.getScale().ifPresent(vec -> matrix.scale(vec.x(), vec.y(), vec.z()));
        this.getOffset().ifPresent(vec -> matrix.translate(vec.x(), vec.y(), vec.z()));
        RenderUtils.renderState(this.state, world, pos, matrix, buffer, light, overlay, this.renderFluid, preferredSides);
        matrix.popPose();
    }

    static class Serializer implements ISerializer<DisplayableBlockState> {

        @Override
        public DisplayableBlockState read (JsonElement json) {

            if (json.isJsonObject()) {

                final JsonObject obj = json.getAsJsonObject();
                final BlockState state = Serializers.BLOCK_STATE.read(obj);
                final Optional<Vector3f> scale = Serializers.VEC3F.readOptional(obj, "scale");
                final Optional<Vector3f> offset = Serializers.VEC3F.readOptional(obj, "offset");
                final boolean renderFluid = Serializers.BOOLEAN.read(obj, "renderFluid", true);
                return new DisplayableBlockState(state, scale, offset, renderFluid);
            }

            throw new JsonParseException("Expected properties to be an object. Recieved " + JSONUtils.getType(json));
        }

        @Override
        public JsonElement write (DisplayableBlockState toWrite) {

            final JsonElement json = Serializers.BLOCK_STATE.write(toWrite.getState());
            final JsonObject obj = (JsonObject) json;
            toWrite.getScale().ifPresent(v -> obj.add("scale", Serializers.VEC3F.writeOptional(toWrite.getScale())));
            toWrite.getOffset().ifPresent(v -> obj.add("offset", Serializers.VEC3F.writeOptional(toWrite.getOffset())));
            obj.addProperty("renderFluid", toWrite.isRenderFluid());
            return obj;
        }

        @Override
        public DisplayableBlockState read (PacketBuffer buffer) {

            final BlockState state = Serializers.BLOCK_STATE.read(buffer);
            final Optional<Vector3f> scale = Serializers.VEC3F.readOptional(buffer);
            final Optional<Vector3f> offset = Serializers.VEC3F.readOptional(buffer);
            final boolean renderFluid = Serializers.BOOLEAN.read(buffer);
            return new DisplayableBlockState(state, scale, offset, renderFluid);
        }

        @Override
        public void write (PacketBuffer buffer, DisplayableBlockState toWrite) {

            Serializers.BLOCK_STATE.write(buffer, toWrite.getState());
            Serializers.VEC3F.writeOptional(buffer, toWrite.getScale());
            Serializers.VEC3F.writeOptional(buffer, toWrite.getOffset());
            Serializers.BOOLEAN.write(buffer, toWrite.isRenderFluid());
        }
    }
}
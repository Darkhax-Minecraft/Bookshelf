package net.darkhax.bookshelf.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public interface FluidRenderer {

    FluidRenderer INSTANCE = Services.load(FluidRenderer.class);

    static FluidRenderer get() {

        return INSTANCE;
    }

    void render(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay);

    default int[] argb(int color) {

        final int[] colors = new int[4];
        colors[0] = color >> 24 & 0xff; // alpha
        colors[1] = color >> 16 & 0xff; // red
        colors[2] = color >> 8 & 0xff; // green
        colors[3] = color & 0xff; // blue
        return colors;
    }

    /**
     * Renders a box with the sprite on each face.
     *
     * @param builder The vertex consumer.
     * @param stack   The pose stack.
     * @param sprite  The sprite to render.
     * @param light   The packed light data.
     * @param overlay The overlay day.
     * @param color   Unpacked color data in ARGB ordering.
     */
    default void renderBox(VertexConsumer builder, PoseStack stack, TextureAtlasSprite sprite, int light, int overlay, int[] color) {

        renderBox(builder, stack.last().pose(), sprite, light, overlay, 0f, 1f, 0f, 1f, 0f, 1f, color);
    }

    /**
     * Renders a box with the sprite on each face.
     *
     * @param builder The vertex consumer.
     * @param stack   The post stack.
     * @param sprite  The sprite to render.
     * @param light   Packed light coordinates.
     * @param overlay Overlay data.
     * @param x1      The starting render position on the X axis.
     * @param x2      The ending render postion on the X axis.
     * @param y1      The starting render position on the Y axis.
     * @param y2      The ending render position on the Y axis.
     * @param z1      The starting render position on the Z axis.
     * @param z2      The ending render position on the Z axis.
     * @param color   Unpacked color data in ARGB ordering.
     */
    default void renderBox(VertexConsumer builder, PoseStack stack, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {

        renderBox(builder, stack.last().pose(), sprite, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }

    /**
     * Renders a box with the sprite on each face.
     *
     * @param builder The vertex consumer.
     * @param pos     The position to render at.
     * @param sprite  The sprite to render.
     * @param light   Packed light coordinates.
     * @param overlay Overlay data.
     * @param x1      The starting render position on the X axis.
     * @param x2      The ending render postion on the X axis.
     * @param y1      The starting render position on the Y axis.
     * @param y2      The ending render position on the Y axis.
     * @param z1      The starting render position on the Z axis.
     * @param z2      The ending render position on the Z axis.
     * @param color   Unpacked color data in ARGB ordering.
     */
    default void renderBox(VertexConsumer builder, Matrix4f pos, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {

        renderFace(builder, pos, sprite, Direction.DOWN, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.UP, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.NORTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.SOUTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.WEST, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.EAST, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }

    /**
     * Renders the face of a box.
     *
     * @param builder The vertex consumer.
     * @param pos     The position to render at.
     * @param sprite  The sprite to render.
     * @param side    The side to render.
     * @param light   Packed light coordinates.
     * @param overlay Overlay data.
     * @param x1      The starting render position on the X axis.
     * @param x2      The ending render postion on the X axis.
     * @param y1      The starting render position on the Y axis.
     * @param y2      The ending render position on the Y axis.
     * @param z1      The starting render position on the Z axis.
     * @param z2      The ending render position on the Z axis.
     * @param color   Unpacked color data in ARGB ordering.
     */
    default void renderFace(VertexConsumer builder, Matrix4f pos, TextureAtlasSprite sprite, Direction side, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {

        // Convert block size to pixel size
        final double px1 = x1 * 16;
        final double px2 = x2 * 16;
        final double py1 = y1 * 16;
        final double py2 = y2 * 16;
        final double pz1 = z1 * 16;
        final double pz2 = z2 * 16;

        switch (side) {

            case DOWN -> {
                final float u1 = sprite.getU(px1);
                final float u2 = sprite.getU(px2);
                final float v1 = sprite.getV(pz1);
                final float v2 = sprite.getV(pz2);
                builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
                builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
                builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
                builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
            }
            case UP -> {
                final float u1 = sprite.getU(px1);
                final float u2 = sprite.getU(px2);
                final float v1 = sprite.getV(pz1);
                final float v2 = sprite.getV(pz2);
                builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
                builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
                builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
                builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
            }
            case NORTH -> {
                final float u1 = sprite.getU(px1);
                final float u2 = sprite.getU(px2);
                final float v1 = sprite.getV(py1);
                final float v2 = sprite.getV(py2);
                builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
                builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
                builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
                builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
            }
            case SOUTH -> {
                final float u1 = sprite.getU(px1);
                final float u2 = sprite.getU(px2);
                final float v1 = sprite.getV(py1);
                final float v2 = sprite.getV(py2);
                builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
                builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
                builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
                builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
            }
            case WEST -> {
                final float u1 = sprite.getU(py1);
                final float u2 = sprite.getU(py2);
                final float v1 = sprite.getV(pz1);
                final float v2 = sprite.getV(pz2);
                builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
                builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
                builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
                builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
            }
            case EAST -> {
                final float u1 = sprite.getU(py1);
                final float u2 = sprite.getU(py2);
                final float v1 = sprite.getV(pz1);
                final float v2 = sprite.getV(pz2);
                builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
                builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
                builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
                builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
            }
        }
    }
}
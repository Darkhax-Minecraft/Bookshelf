package net.darkhax.bookshelf.common.api.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.joml.Matrix4f;

public interface IRenderHelper {

    IRenderHelper GET = Services.load(IRenderHelper.class);

    default TextureAtlasSprite blockSprite(Identifier texturePath) {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(texturePath);
    }

    void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, int light, int overlay);

    default int[] unpackARGB(int color) {
        return new int[]{color >> 24 & 0xff, color >> 16 & 0xff, color >> 8 & 0xff, color & 0xff};
    }

    default void renderBox(VertexConsumer builder, PoseStack stack, TextureAtlasSprite sprite, int light, int overlay, int[] color) {
        renderBox(builder, stack.last().pose(), sprite, light, overlay, 0f, 1f, 0f, 1f, 0f, 1f, color);
    }

    default void renderBox(VertexConsumer builder, PoseStack stack, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        renderBox(builder, stack.last().pose(), sprite, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }

    default void renderBox(VertexConsumer builder, Matrix4f pos, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        renderFace(builder, pos, sprite, Direction.DOWN, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.UP, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.NORTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.SOUTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.WEST, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderFace(builder, pos, sprite, Direction.EAST, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }

    default void renderFace(VertexConsumer builder, Matrix4f pos, TextureAtlasSprite sprite, Direction side, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        switch (side) {
            case DOWN -> {
                final float u1 = sprite.getU(x1);
                final float u2 = sprite.getU(x2);
                final float v1 = sprite.getV(z1);
                final float v2 = sprite.getV(z2);
                builder.addVertex(pos, x1, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(0f, -1f, 0f);
                builder.addVertex(pos, x1, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0f, -1f, 0f);
                builder.addVertex(pos, x2, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(0f, -1f, 0f);
                builder.addVertex(pos, x2, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0f, -1f, 0f);
            }
            case UP -> {
                final float u1 = sprite.getU(x1);
                final float u2 = sprite.getU(x2);
                final float v1 = sprite.getV(z1);
                final float v2 = sprite.getV(z2);
                builder.addVertex(pos, x1, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
                builder.addVertex(pos, x2, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
                builder.addVertex(pos, x2, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
                builder.addVertex(pos, x1, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
            }
            case NORTH -> {
                final float u1 = sprite.getU(x1);
                final float u2 = sprite.getU(x2);
                final float v1 = sprite.getV(y1);
                final float v2 = sprite.getV(y2);
                builder.addVertex(pos, x1, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0f, 0f, -1f);
                builder.addVertex(pos, x1, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(0f, 0f, -1f);
                builder.addVertex(pos, x2, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0f, 0f, -1f);
                builder.addVertex(pos, x2, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(0f, 0f, -1f);
            }
            case SOUTH -> {
                final float u1 = sprite.getU(x1);
                final float u2 = sprite.getU(x2);
                final float v1 = sprite.getV(y1);
                final float v2 = sprite.getV(y2);
                builder.addVertex(pos, x2, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(0f, 0f, 1f);
                builder.addVertex(pos, x2, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0f, 0f, 1f);
                builder.addVertex(pos, x1, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(0f, 0f, 1f);
                builder.addVertex(pos, x1, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0f, 0f, 1f);
            }
            case WEST -> {
                final float u1 = sprite.getU(y1);
                final float u2 = sprite.getU(y2);
                final float v1 = sprite.getV(z1);
                final float v2 = sprite.getV(z2);
                builder.addVertex(pos, x1, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(-1f, 0f, 0f);
                builder.addVertex(pos, x1, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(-1f, 0f, 0f);
                builder.addVertex(pos, x1, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(-1f, 0f, 0f);
                builder.addVertex(pos, x1, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(-1f, 0f, 0f);
            }
            case EAST -> {
                final float u1 = sprite.getU(y1);
                final float u2 = sprite.getU(y2);
                final float v1 = sprite.getV(z1);
                final float v2 = sprite.getV(z2);
                builder.addVertex(pos, x2, y1, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(1f, 0f, 0f);
                builder.addVertex(pos, x2, y2, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(1f, 0f, 0f);
                builder.addVertex(pos, x2, y2, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(1f, 0f, 0f);
                builder.addVertex(pos, x2, y1, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(1f, 0f, 0f);
            }
        }
    }
}
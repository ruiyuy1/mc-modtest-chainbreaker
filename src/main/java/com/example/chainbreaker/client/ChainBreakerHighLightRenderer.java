package com.example.chainbreaker.client;

import java.util.HashSet;
import java.util.Set;

import org.joml.Matrix4f;

import com.example.chainbreaker.Config;
import com.example.chainbreaker.utils.ChainBreakerScanner;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = "chainbreaker", value = Dist.CLIENT)
public class ChainBreakerHighLightRenderer {

    @SubscribeEvent
    public static void onRenderHighlight(RenderHighlightEvent.Block event) {
        if (ChainBreakerClientEvent.CHAIN_KEY.isDown()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || !ChainBreakerClientEvent.CHAIN_KEY.isDown()) return;

        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;
        BlockPos targetPos = ((BlockHitResult) hit).getBlockPos();
        Set<BlockPos> blocks = ChainBreakerScanner.findMatchBlocks(mc.level, targetPos, mc.player, Config.MAX_BLOCKS.get());
        drawOutline(event, blocks);
    }

    private static void drawOutline(RenderLevelStageEvent event, Set<BlockPos> blocks) {
        PoseStack poseStack = event.getPoseStack();
        Vec3 cam = event.getCamera().getPosition();
        VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource()
                .getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f matrix = poseStack.last().pose();

        Set<Long> drawn = new HashSet<>();
        for (BlockPos pos : blocks) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();

            for (int dy = 0; dy <= 1; dy++)
                for (int dz = 0; dz <= 1; dz++)
                    emitEdge(drawn, blocks, buffer, matrix, x, y + dy, z + dz, 0);

            for (int dx = 0; dx <= 1; dx++)
                for (int dz = 0; dz <= 1; dz++)
                    emitEdge(drawn, blocks, buffer, matrix, x + dx, y, z + dz, 1);

            for (int dx = 0; dx <= 1; dx++)
                for (int dy = 0; dy <= 1; dy++)
                    emitEdge(drawn, blocks, buffer, matrix, x + dx, y + dy, z, 2);
        }

        poseStack.popPose();
    }

    private static void emitEdge(Set<Long> drawn, Set<BlockPos> blocks,
                                  VertexConsumer buffer, Matrix4f matrix,
                                  int ex, int ey, int ez, int axis) {
        long key = edgeKey(ex, ey, ez, axis);
        if (!drawn.add(key)) return;

        boolean a, b, c, d;
        switch (axis) {
            case 0 -> {
                a = blocks.contains(new BlockPos(ex, ey, ez));
                b = blocks.contains(new BlockPos(ex, ey, ez - 1));
                c = blocks.contains(new BlockPos(ex, ey - 1, ez - 1));
                d = blocks.contains(new BlockPos(ex, ey - 1, ez));
            }
            case 1 -> {
                a = blocks.contains(new BlockPos(ex, ey, ez));
                b = blocks.contains(new BlockPos(ex - 1, ey, ez));
                c = blocks.contains(new BlockPos(ex - 1, ey, ez - 1));
                d = blocks.contains(new BlockPos(ex, ey, ez - 1));
            }
            default -> {
                a = blocks.contains(new BlockPos(ex, ey, ez));
                b = blocks.contains(new BlockPos(ex, ey - 1, ez));
                c = blocks.contains(new BlockPos(ex - 1, ey - 1, ez));
                d = blocks.contains(new BlockPos(ex - 1, ey, ez));
            }
        }

        if ((a == b && c == d) || (a == d && b == c)) return;

        float x2 = ex, y2 = ey, z2 = ez;
        float nx = 0, ny = 0, nz = 0;
        switch (axis) {
            case 0 -> { x2 += 1; nx = 1; }
            case 1 -> { y2 += 1; ny = 1; }
            default -> { z2 += 1; nz = 1; }
        }
        
        buffer.addVertex(matrix, (float) ex, (float) ey, (float) ez)
                .setColor(1.0f, 1.0f, 1.0f, 0.4f).setNormal(nx, ny, nz);
        buffer.addVertex(matrix, x2, y2, z2)
                .setColor(1.0f, 1.0f, 1.0f, 0.4f).setNormal(nx, ny, nz);
    }

    private static long edgeKey(int x, int y, int z, int axis) {
        long lx = (long) (x + 30000001) & 0x3FFFFFFL;
        long lz = (long) (z + 30000001) & 0x3FFFFFFL;
        long ly = (long) (y + 65) & 0x3FFL;
        return (lx << 38) | (lz << 12) | (ly << 2) | axis;
    }
}

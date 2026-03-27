package com.example.chainbreaker.client;

import com.example.chainbreaker.Config;
import com.example.chainbreaker.utils.ChainBreakerScanner;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Set;

@EventBusSubscriber(modid = "chainbreaker", value = Dist.CLIENT)
public class ChainBreakerHighLightRenderer {
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || ! ChainBreakerClientEvent.CHAIN_KEY.isDown()) return;

        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;
        BlockPos targetPos = ((BlockHitResult) hit).getBlockPos();
        Set<BlockPos> blocks = ChainBreakerScanner.findMatchBlocks(mc.level, targetPos, Config.MAX_BLOCKS.get());
        drawSelectionBoxes(event, blocks);
    }


    private static void drawSelectionBoxes(RenderLevelStageEvent event, Set<BlockPos> blocks) {
        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();

        VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

        poseStack.pushPose();

        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        for (BlockPos pos : blocks) {
            LevelRenderer.renderLineBox(
                    poseStack, buffer,
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1,
                    0.0F, 1.0F, 0.0F, 0.4F // 绿色 (RGB: 0,1,0), Alpha: 0.4
            );
        }

        poseStack.popPose();
    }
}

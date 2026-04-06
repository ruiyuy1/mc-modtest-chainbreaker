package com.example.chainbreaker.handler;

import com.example.chainbreaker.ChainBreaker;
import com.example.chainbreaker.Config;
import com.example.chainbreaker.state.PlayerStateStore;
import com.example.chainbreaker.utils.ChainBreakerScanner;
import com.example.chainbreaker.utils.ActionExecutor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Set;

@EventBusSubscriber(modid = ChainBreaker.MODID)
public final class ChainBreakerUseHandler {

    private static boolean isChainUse = false;

    private ChainBreakerUseHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isChainUse) return;

        Level level = event.getLevel();
        if (level.isClientSide) return;

        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!PlayerStateStore.isChainModeActive(player.getUUID())) return;
        if (serverPlayer.isShiftKeyDown()) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        BlockPos startPos = event.getPos();
        if (level.getBlockState(startPos).isAir()) return;

        Set<BlockPos> blocks = ChainBreakerScanner.findMatchBlocks(level, startPos, player, Config.MAX_BLOCKS.get());
        if (blocks.size() <= 1) return;

        Direction face = event.getHitVec().getDirection();

        isChainUse = true;
        try {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);

            ActionExecutor.executeInChain(serverPlayer, blocks, startPos, true, pos -> {
                ItemStack handStack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
                if (handStack.isEmpty()) return false;

                BlockHitResult hit = new BlockHitResult(
                        Vec3.atCenterOf(pos).relative(face, 0.5),
                        face,
                        pos,
                        false
                );
                UseOnContext ctx = new UseOnContext(level, serverPlayer, InteractionHand.MAIN_HAND, handStack, hit);
                handStack.useOn(ctx);
                return true;
            });
        } finally {
            isChainUse = false;
        }
    }
}

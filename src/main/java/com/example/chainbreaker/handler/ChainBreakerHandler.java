package com.example.chainbreaker.handler;

import com.example.chainbreaker.Config;
import com.example.chainbreaker.state.PlayerStateStore;
import com.example.chainbreaker.utils.ChainBreakerScanner;
import com.example.chainbreaker.utils.ActionExecutor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Set;

import net.minecraft.world.item.ItemStack;

@EventBusSubscriber(modid = "chainbreaker")
public class ChainBreakerHandler {

    private static boolean isMining = false;

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (isMining) return;

        Player player = event.getPlayer();
        if (!PlayerStateStore.isChainModeActive(player.getUUID())) return;

        Level level = (Level) event.getLevel();
        BlockPos startPos = event.getPos();

        Set<BlockPos> blocksSelected = ChainBreakerScanner.findMatchBlocks(
                level, startPos, player, Config.MAX_BLOCKS.get()
        );

        if (player instanceof ServerPlayer serverPlayer) {
            isMining = true;
            try {
                ActionExecutor.executeInChain(serverPlayer, blocksSelected, startPos, false, pos -> {
                    serverPlayer.gameMode.destroyBlock(pos);
                    return true;
                });
            } finally {
                isMining = false;
            }
        }
    }
}

package com.example.chainbreaker.handler;

import com.example.chainbreaker.Config;
import com.example.chainbreaker.state.PlayerStateStore;
import com.example.chainbreaker.utils.ChainBreakerScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@EventBusSubscriber(modid = "chainbreaker")
public class ChainBreakerHandler {

    private static boolean isMining = false;
    private static final Queue<MiningTask> TASK_QUEUE = new ConcurrentLinkedDeque<>();

    private record MiningTask(ServerPlayer player, BlockPos pos) {}

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (isMining) return;

        Player player = event.getPlayer();
        if (PlayerStateStore.isChainModeActive(player.getUUID())) {
            Level level = (Level) event.getLevel();
            BlockPos startPos = event.getPos();

            Set<BlockPos> blocksToMine = ChainBreakerScanner.findMatchBlocks(
                    level,
                    startPos,
                    Config.MAX_BLOCKS.get()
            );

            if (player instanceof ServerPlayer serverPlayer) {
                for (BlockPos pos : blocksToMine) {
                    if (!pos.equals(startPos)) {
                        TASK_QUEUE.add(new MiningTask(serverPlayer, pos));
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (TASK_QUEUE.isEmpty()) return;

        isMining = true;
        try {
            for (int i = 0; i < Config.MAX_BLOCK_PER_TICKS.get(); i++) {
                MiningTask task = TASK_QUEUE.poll();
                if (task == null) break;

                if (task.player() != null && !task.player().isRemoved()) {
                    task.player().gameMode.destroyBlock(task.pos());
                }
            }
        } finally {
            isMining = false;
        }
    }
}

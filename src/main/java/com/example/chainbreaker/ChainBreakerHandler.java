package com.example.chainbreaker;

import com.example.chainbreaker.state.PlayerStateStore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import javax.swing.*;
import java.util.*;

@EventBusSubscriber(modid = "chainbreaker")
public class ChainBreakerHandler {

    private static boolean isMining = false;
    private static final Queue<MiningTask> TASK_QUEUE = new java.util.concurrent.ConcurrentLinkedQueue<>();

    private record MiningTask(ServerPlayer player, BlockPos pos) {}

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (isMining) return;
        Player player = event.getPlayer();
        if (PlayerStateStore.isChainModeActive(player.getUUID())){
            try{
                    BlockPos startPos = event.getPos();
                    BlockState startState = event.getState();
                    Block startBlock = startState.getBlock();
                    Queue<BlockPos> queue = new LinkedList<>();
                    Set<BlockPos> visited = new HashSet<>();
                    int count = 0;
                    Level level = (Level) event.getLevel();

                    queue.add(startPos);
                    visited.add(startPos);
                    while(!queue.isEmpty()){
                        BlockPos currPos = queue.poll();
                        for (BlockPos neighbor : BlockPos.betweenClosed(currPos.offset(-1, -1, -1), currPos.offset(1, 1, 1))) {
                            BlockPos nextPos = neighbor.immutable();
                            if ((level.getBlockState(nextPos).is(startBlock)) && !visited.contains(nextPos)){
                                queue.add(nextPos);
                                visited.add(nextPos);
                                if (player instanceof ServerPlayer serverPlayer) {
                                    TASK_QUEUE.add(new MiningTask(serverPlayer, nextPos));
                                }
                                count++;

                            }
                            if (count >= Config.MAX_BLOCKS.get()){
                                break;
                            }
                        }
                    }
            } catch (Exception e) {
                throw new RuntimeException(e);
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

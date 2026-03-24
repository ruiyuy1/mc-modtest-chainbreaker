package com.example.chainbreaker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import javax.swing.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@EventBusSubscriber(modid = "chainbreaker")
public class ChainBreakerHandler {

    private static boolean isMining = false;
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (isMining) return;
        Player player = event.getPlayer();
        if (player.isShiftKeyDown()){
            isMining = true;
            try{
                    BlockPos startPos = event.getPos();
                    BlockState startState = event.getState();
                    Block startBlock = startState.getBlock();
                    Queue<BlockPos> queue = new LinkedList<>();
                    Set<BlockPos> visited = new HashSet<>();
                    int maxCount = 64;
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
                                    serverPlayer.gameMode.destroyBlock(nextPos);
                                }
                                count++;

                            }
                            if (count >= maxCount){
                                break;
                            }
                        }
                    }
            }finally {
                isMining = false;
            }

        }
    }
}

package com.example.chainbreaker.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ChainBreakerScanner {

        public static Set<BlockPos> findMatchBlocks(Level level, BlockPos startPos, int maxBlocks) {
            Set<BlockPos> visited = new LinkedHashSet<>();
            Queue<BlockPos> queue = new LinkedList<>();

            Block startBlock = level.getBlockState(startPos).getBlock();

            queue.add(startPos);
            visited.add(startPos);

            while (!queue.isEmpty() && visited.size() < maxBlocks) {
                BlockPos currPos = queue.poll();

                for (BlockPos neighbor : BlockPos.betweenClosed(currPos.offset(-1, -1, -1), currPos.offset(1, 1, 1))) {
                    BlockPos nextPos = neighbor.immutable();

                    if (!visited.contains(nextPos) && level.getBlockState(nextPos).is(startBlock)) {
                        visited.add(nextPos);
                        queue.add(nextPos);

                        if (visited.size() >= maxBlocks) return visited;
                    }
                }
            }
            return visited;
        }
    }
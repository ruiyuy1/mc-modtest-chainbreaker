package com.example.chainbreaker.scanner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.Set;

public interface BlockScanner {
    Set<BlockPos> scan(Level level, BlockPos startPos, Player player, int maxBlocks);
}
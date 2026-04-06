package com.example.chainbreaker.utils;

import com.example.chainbreaker.scanner.BlockScanner;
import com.example.chainbreaker.scanner.VeinScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;

public class ChainBreakerScanner {
    
    // Default to VeinScanner for now. You can expand this logic to select different scanners (like Tunnel) based on active state or tool type.
    private static BlockScanner activeScanner = new VeinScanner();

    public static void setScanner(BlockScanner scanner) {
        activeScanner = scanner;
    }

    public static Set<BlockPos> findMatchBlocks(Level level, BlockPos startPos, Player player, int maxBlocks) {
        return activeScanner.scan(level, startPos, player, maxBlocks);
    }
}
package com.example.chainbreaker.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.function.Predicate;

public class ActionExecutor {
    public static void executeInChain(ServerPlayer player, Set<BlockPos> blocks, BlockPos startPos, boolean includeStartPos, Predicate<BlockPos> action) {
        for (BlockPos pos : blocks) {
            if (!includeStartPos && pos.equals(startPos)) continue;

            ItemStack handStack = player.getMainHandItem();
            if (handStack.isDamageableItem() && handStack.getDamageValue() >= handStack.getMaxDamage() - 2) {
                break;
            }

            if (!action.test(pos)) {
                break;
            }
        }
    }
}
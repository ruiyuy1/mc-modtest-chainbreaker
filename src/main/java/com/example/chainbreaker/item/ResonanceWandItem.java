package com.example.chainbreaker.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ResonanceWandItem extends Item {

    public ResonanceWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        
        if (!level.isClientSide()) {
            player.displayClientMessage(Component.literal("Resonance Wand used!"), true);
        }

        player.getCooldowns().addCooldown(this, 20); // 1 second cooldown
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}

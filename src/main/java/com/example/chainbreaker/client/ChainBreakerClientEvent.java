package com.example.chainbreaker.client;

import com.example.chainbreaker.ChainBreaker;
import com.example.chainbreaker.network.ChainKeyPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ChainBreaker.MODID, value = Dist.CLIENT)
public class ChainBreakerClientEvent {
    public static final KeyMapping CHAIN_KEY = new KeyMapping(
            "key.chainbreaker.toggle",
            InputConstants.KEY_GRAVE,
            "key.categories.chainbreaker"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        event.register(CHAIN_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ChainBreakerClientEvent.CHAIN_KEY.isDown()) {
            PacketDistributor.sendToServer(new ChainKeyPacket(true));
        }else{
            PacketDistributor.sendToServer(new ChainKeyPacket(false));
        }
    }
}

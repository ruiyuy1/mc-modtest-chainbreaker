package com.example.chainbreaker;

import com.example.chainbreaker.network.ChainKeyPacket;
import com.example.chainbreaker.registry.ModCreativeTabs;
import com.example.chainbreaker.registry.ModItems;
import com.example.chainbreaker.state.PlayerStateStore;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(ChainBreaker.MODID)
public class ChainBreaker {
    public static final String MODID = "chainbreaker";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChainBreaker(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::registerNetwork);
    }

    private void registerNetwork(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ChainKeyPacket.TYPE,
                ChainKeyPacket.CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        PlayerStateStore.setChainMode(context.player().getUUID(), payload.isPressed());
                    });
                }
        );
    }
}

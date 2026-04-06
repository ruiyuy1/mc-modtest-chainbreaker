package com.example.chainbreaker.registry;

import com.example.chainbreaker.ChainBreaker;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChainBreaker.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHAINBREAKER_TAB = TABS.register("chainbreaker_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.chainbreaker"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.CHAIN_CORE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.CHAIN_CORE.get());
            }).build());
}

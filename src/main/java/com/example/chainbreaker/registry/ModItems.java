package com.example.chainbreaker.registry;

import com.example.chainbreaker.ChainBreaker;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChainBreaker.MODID);

    public static final DeferredItem<Item> CHAIN_GEM = ITEMS.registerSimpleItem("chain_gem", new Item.Properties());
}

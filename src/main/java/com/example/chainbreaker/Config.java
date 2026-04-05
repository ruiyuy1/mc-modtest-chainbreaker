package com.example.chainbreaker;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_BLOCKS = BUILDER
            .comment("Maximum number of blocks chain breaker can break")
            .defineInRange("maxBlocks", 64, 1, 1024);

    static final ModConfigSpec SPEC = BUILDER.build();
}

package com.example.chainbreaker.state;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStateStore {
    private static final Map<UUID, Boolean> PLAYER_MODES = new ConcurrentHashMap<>();

    public static void setChainMode(UUID uuid, boolean active) {
        PLAYER_MODES.put(uuid, active);
    }

    public static boolean isChainModeActive(UUID uuid) {
        return PLAYER_MODES.getOrDefault(uuid, false);
    }
}
package com.example.chainbreaker.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChainKeyPacket(boolean isPressed) implements CustomPacketPayload {
    public static final Type<ChainKeyPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("chainbreaker", "key_packet")
    );

    public static final StreamCodec<FriendlyByteBuf, ChainKeyPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ChainKeyPacket::isPressed,
            ChainKeyPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
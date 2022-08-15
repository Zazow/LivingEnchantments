package com.zazow.livingenchantments.network;

import com.zazow.livingenchantments.attributes.VeinTalent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketState {
    private final boolean activate;
    public CPacketState(boolean activate) {
        this.activate = activate;
    }

    public static void encode(CPacketState msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.activate);
    }

    public static CPacketState decode(FriendlyByteBuf buf) {
        return new CPacketState(buf.readBoolean());
    }

    public static void handle(CPacketState msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

            if (sender != null) {

                if (msg.activate) {
                    VeinTalent.playersWantingToVeinMine.put(sender.getUUID(), sender.level.getGameTime());
                } else {
                    VeinTalent.playersWantingToVeinMine.remove(sender.getUUID());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
package com.zazow.livingenchantments.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.zazow.livingenchantments.attributes.VeinTalent;
import com.zazow.livingenchantments.network.LENetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandlers {
    public static final KeyMapping key = new KeyMapping("key.livingenchantments.vein", InputConstants.UNKNOWN.getValue(), "key.livingenchantments.category");
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void veinMiningState(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            LocalPlayer player = mc.player;

            if (world == null || player == null) {
                return;
            }

            if (world.getGameTime() % 5 != 0) {
                return;
            }

            boolean enabled = key.isDown();
            LENetwork.sendC2SState(enabled);
        }
    }
}

package com.zazow.livingenchantments.network;

import com.zazow.livingenchantments.LEMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LENetwork {

    private static final String PTC_VERSION = "1";
    private static SimpleChannel instance;
    private static int id = 0;

    public static void register() {
        instance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(LEMod.MODID, "network"))
                .networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();
        registerMessage(CPacketState.class, CPacketState::encode, CPacketState::decode, CPacketState::handle);

    }

    private static <T> void registerMessage(
            Class<T> messageType,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        instance.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    public static void sendC2SState(boolean activate) {
        LENetwork.instance.send(PacketDistributor.SERVER.noArg(), new CPacketState(activate));
    }
}

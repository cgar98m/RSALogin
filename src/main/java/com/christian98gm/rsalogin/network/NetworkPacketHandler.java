package com.christian98gm.rsalogin.network;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkPacketHandler
{
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RSALogin.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private NetworkPacketHandler()
    {
        throw new UnsupportedOperationException("Instance required");
    }

    public static void registerPackets()
    {
        registerPacket(MessageLoginRequest.class,
                MessageLoginRequest::encode,
                MessageLoginRequest::decode,
                MessageLoginRequest::handle,
                NetworkDirection.PLAY_TO_CLIENT);
        registerPacket(MessageLoginResponse.class,
                MessageLoginResponse::encode,
                MessageLoginResponse::decode,
                MessageLoginResponse::handle,
                NetworkDirection.PLAY_TO_SERVER);
    }

    private static int id = 0;
    private static <MSG> void registerPacket(Class<MSG> msg, BiConsumer<MSG, FriendlyByteBuf> encoder,
                                             Function<FriendlyByteBuf, MSG> decoder,
                                             BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler,
                                             final NetworkDirection direction)
    {
        INSTANCE.registerMessage(id++, msg, encoder, decoder, handler, Optional.of(direction));
    }
}

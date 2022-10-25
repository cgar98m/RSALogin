package com.christian98gm.rsalogin.network.packet;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.client.handler.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageLoginRequest
{
    public MessageLoginRequest()
    {
    }

    public static void encode(MessageLoginRequest msg, FriendlyByteBuf buf)
    {
        //No content message
    }

    public static MessageLoginRequest decode(FriendlyByteBuf buf)
    {
        return new MessageLoginRequest();
    }

    public static void handle(MessageLoginRequest msg, Supplier<NetworkEvent.Context> ctx)
    {
        RSALogin.LOGGER.debug("MessageLoginRequest received");
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> ClientPacketHandler.LoginRequestHandler.handlePacket(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
}
